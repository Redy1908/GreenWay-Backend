package dev.redy1908.greenway.osrm.domain;

import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.LatLng;
import dev.redy1908.greenway.delivery.domain.Delivery;
import dev.redy1908.greenway.dem.domain.DemRepository;
import dev.redy1908.greenway.osrm.domain.exceptions.models.CantConnectToOsrmException;
import dev.redy1908.greenway.osrm.domain.exceptions.models.InvalidOsrmResponseException;
import dev.redy1908.greenway.osrm.domain.exceptions.models.PointOutOfBoundsException;
import dev.redy1908.greenway.vehicle_deposit.domain.VehicleDeposit;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
class OsrmServiceImpl implements IOsrmService {

    @Value("${osrm.route-standard-url}")
    private String OSRM_ROUTE_STANDARD_URL;

    @Value("${osrm.route-elevation-url}")
    private String OSRM_ROUTE_ELEVATION_URL;

    @Value("${osrm.table-elevation-url}")
    private String OSRM_TABLE_ELEVATION_URL;

    @Value("${osrm.lon-min}")
    private double lonMin;

    @Value("${osrm.lon-max}")
    private double lonMax;

    @Value("${osrm.lat-min}")
    private double latMin;

    @Value("${osrm.lat-max}")
    private double latMax;

    private final RestTemplate restTemplate;

    private final DemRepository demRepository;

    @Override
    public Map<String, Object> getNavigationData(Point startingPoint, List<Point> wayPoints, NavigationType navigationType) {
        String osrmUrl = buildOsrmUrl(startingPoint, wayPoints, navigationType);
        Map<String, Object> osrmResponse = getOsrmResponse(osrmUrl);
        String polyline = extractPolylineFromOsrmResponse(osrmResponse);
        LineString lineString = polylineToLineString(polyline);
        List<Double> elevations = demRepository.findValuesForLineString(lineString.toString());
        osrmResponse.put("elevations", elevations);
        return osrmResponse;
    }

    private Map<String, Object> getOsrmResponse(String url) {
        try {
            Map<String, Object> osrmResponse = restTemplate.getForObject(url, Map.class);

            if (osrmResponse == null) {
                throw new InvalidOsrmResponseException();
            }

            return osrmResponse;
        } catch (RestClientException e) {
            throw new CantConnectToOsrmException();
        }
    }

    @Override
    public void checkPointInBounds(Point point) {

        if (point.getX() < lonMin || point.getX() > lonMax || point.getY() < latMin || point.getY() > latMax) {
            throw new PointOutOfBoundsException(point, lonMin, lonMax, latMin, latMax);
        }
    }

    @Override
    public Pair<double[][], double[][]> getMatrixDistances(VehicleDeposit vehicleDeposit, List<Delivery> deliveryList) {

        String url = buildUrlMatrix(vehicleDeposit, deliveryList);

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        JSONObject jsonObject = new JSONObject(response.getBody());

        JSONArray durations = jsonObject.getJSONArray("durations");
        JSONArray distances = jsonObject.getJSONArray("distances");

        double[][] matrixDurations = new double[durations.length()][];
        double[][] matrixDistances = new double[distances.length()][];

        for (int i = 0; i < durations.length(); i++) {
            JSONArray rowDurations = durations.getJSONArray(i);
            JSONArray rowDistances = distances.getJSONArray(i);
            matrixDurations[i] = new double[rowDurations.length()];
            matrixDistances[i] = new double[rowDistances.length()];
            for (int j = 0; j < rowDurations.length(); j++) {
                matrixDurations[i][j] = rowDurations.getDouble(j);
                matrixDistances[i][j] = rowDistances.getDouble(j);
            }
        }

        return Pair.of(matrixDurations, matrixDistances);
    }

    private String buildUrlMatrix(VehicleDeposit vehicleDeposit, List<Delivery> deliveryList) {
        StringBuilder urlBuilder = new StringBuilder(OSRM_TABLE_ELEVATION_URL);

        urlBuilder.append(vehicleDeposit.getDepositCoordinates().getX());
        urlBuilder.append(",");
        urlBuilder.append(vehicleDeposit.getDepositCoordinates().getY());
        urlBuilder.append(";");

        for (Delivery delivery : deliveryList) {
            urlBuilder.append(delivery.getReceiverCoordinates().getX());
            urlBuilder.append(",");
            urlBuilder.append(delivery.getReceiverCoordinates().getY());
            urlBuilder.append(";");
        }

        urlBuilder.deleteCharAt(urlBuilder.length() - 1);

        urlBuilder.append("?annotations=duration,distance");

        return urlBuilder.toString();
    }

    private String buildOsrmUrl(Point startingPoint, List<Point> wayPoints, NavigationType navigationType) {

        String baseUrl;

        if (navigationType == NavigationType.STANDARD) {
            baseUrl = OSRM_ROUTE_STANDARD_URL;
        } else {
            baseUrl = OSRM_ROUTE_ELEVATION_URL;
        }

        String wayPointsString = wayPoints.stream().map(
                        point -> String.format(Locale.US, "%f,%f", point.getX(), point.getY()))
                .collect(Collectors.joining(";"));

        return String.format(Locale.US, "%s%f,%f;%s?steps=true&overview=full", baseUrl, startingPoint.getX(), startingPoint.getY(), wayPointsString);
    }

    private String extractPolylineFromOsrmResponse(Map<String, Object> osrmResponse) {

        if (osrmResponse.containsKey("routes")) {
            List<Object> routes = (List<Object>) osrmResponse.get("routes");
            if (!routes.isEmpty()) {
                Map<String, Object> firstRoute = (Map<String, Object>) routes.getFirst();
                if (firstRoute.containsKey("geometry")) {
                    return String.valueOf(firstRoute.get("geometry"));
                }
            }
        }

        throw new InvalidOsrmResponseException();
    }

    private LineString polylineToLineString(String polyline) {
        List<LatLng> latLngs = PolylineEncoding.decode(polyline);

        List<Coordinate> coordinates = new ArrayList<>();
        for (com.google.maps.model.LatLng latLng : latLngs) {
            coordinates.add(new Coordinate(latLng.lng, latLng.lat));
        }

        GeometryFactory geometryFactory = new GeometryFactory();
        return geometryFactory.createLineString(coordinates.toArray(new Coordinate[0]));
    }

}



