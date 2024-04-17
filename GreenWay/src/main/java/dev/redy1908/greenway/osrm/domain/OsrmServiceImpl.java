package dev.redy1908.greenway.osrm.domain;

import dev.redy1908.greenway.delivery.domain.Delivery;
import dev.redy1908.greenway.delivery_vehicle.domain.DeliveryVehicle;
import dev.redy1908.greenway.osrm.domain.exceptions.models.InvalidOsrmResponseException;
import dev.redy1908.greenway.osrm.domain.exceptions.models.PointOutOfBoundsException;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
class OsrmServiceImpl implements IOsrmService {

    @Value("${osrm.route-url}")
    private String OSRM_ROUTE_URL;

    @Value("${osrm.table-url}")
    private String OSRM_TABLE_URL;

    @Value("${opentopodata.url}")
    private String OPENTOPODATA_URL;

    @Value("${osrm.lon-min}")
    private double lonMin;

    @Value("${osrm.lon-max}")
    private double lonMax;

    @Value("${osrm.lat-min}")
    private double latMin;

    @Value("${osrm.lat-max}")
    private double latMax;

    private final RestTemplate restTemplate;

    @Override
    public Map<String, Object> getNavigationData(Point startingPoint, List<Point> wayPoints) {
        String url = buildUrlRoute(startingPoint, wayPoints);
        return getOsrmResponse(url);
    }

    @Override
    public Map<String, Object> getElevationData(Point startingPoint, List<Point> wayPoints) {
        String url = buildUrlElevation(startingPoint, wayPoints);
        Map<String, Object> osrmResponse = getOsrmResponse(url);

        String polyline = extractPolylineFromOsrmResponse(osrmResponse);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(OPENTOPODATA_URL).queryParam("locations", polyline);
        URI uri = builder.build().encode().toUri();

        return restTemplate.getForObject(uri, Map.class);
    }

    private Map<String, Object> getOsrmResponse(String url) {
        Map<String, Object> osrmResponse = restTemplate.getForObject(url, Map.class);

        if (osrmResponse == null) {
            throw new InvalidOsrmResponseException();
        }

        return osrmResponse;
    }

    @Override
    public void checkPointInBounds(Point point) {

        if (point.getX() < lonMin || point.getX() > lonMax || point.getY() < latMin || point.getY() > latMax) {
            throw new PointOutOfBoundsException(point, lonMin, lonMax, latMin, latMax);
        }
    }

    @Override
    public Pair<double[][], double[][]> getMatrixDistances(List<DeliveryVehicle> deliveryVehicleList, List<Delivery> deliveryList) {

        String url = buildUrlMatrix(deliveryVehicleList, deliveryList);

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

    private String buildUrlMatrix(List<DeliveryVehicle> deliveryVehicleList, List<Delivery> deliveryList) {
        StringBuilder urlBuilder = new StringBuilder(OSRM_TABLE_URL);

        DeliveryVehicle deliveryVehicle = deliveryVehicleList.getFirst();
        urlBuilder.append(deliveryVehicle.getDepositCoordinates().getX());
        urlBuilder.append(",");
        urlBuilder.append(deliveryVehicle.getDepositCoordinates().getY());
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

    private String buildUrlRoute(Point startingPoint, List<Point> wayPoints) {

        return OSRM_ROUTE_URL + startingPoint.getX() + "," + startingPoint.getY() + ";" + wayPoints.stream()
                .map(point -> point.getX() + "," + point.getY())
                .collect(Collectors.joining(";")) + "?steps=true&overview=full";
    }

    private String buildUrlElevation(Point startingPoint, List<Point> wayPoints) {

        return OSRM_ROUTE_URL + startingPoint.getX() + "," + startingPoint.getY() + ";" + wayPoints.stream()
                .map(point -> point.getX() + "," + point.getY())
                .collect(Collectors.joining(";"));
    }

    private String extractPolylineFromOsrmResponse(Map<String, Object> osrmResponse) {

        if (osrmResponse.containsKey("routes")) {
            List<Object> trips = (List<Object>) osrmResponse.get("routes");
            if (!trips.isEmpty()) {
                Map<String, Object> firstRoute = (Map<String, Object>) trips.getFirst();
                if (firstRoute.containsKey("geometry")) {
                    return String.valueOf(firstRoute.get("geometry"));
                }
            }
        }

        throw new InvalidOsrmResponseException();
    }
}
