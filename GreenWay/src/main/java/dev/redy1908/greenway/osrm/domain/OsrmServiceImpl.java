package dev.redy1908.greenway.osrm.domain;

import dev.redy1908.greenway.app.web.exceptions.GenericException;
import dev.redy1908.greenway.delivery.domain.Delivery;
import dev.redy1908.greenway.osrm.domain.exceptions.models.*;
import dev.redy1908.greenway.vehicle_deposit.domain.VehicleDeposit;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
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

    @Value("${osrm.route-standard-url}")
    private String OSRM_ROUTE_STANDARD_URL;

    @Value("${osrm.route-elevation-url}")
    private String OSRM_ROUTE_ELEVATION_URL;

    @Value("${osrm.table-elevation-url}")
    private String OSRM_TABLE_ELEVATION_URL;

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
    public Map<String, Object> getNavigationData(Point startingPoint, List<Point> wayPoints, NavigationType navigationType) {
        String url = buildUrl(startingPoint, wayPoints, navigationType);
        Map<String, Object> osrmResponse = getOsrmResponse(url);
        String polyline = extractPolylineFromOsrmResponse(osrmResponse);
        Map<String, Object> openTopodataResponse = getElevationData(polyline);
        return addElevation(osrmResponse, openTopodataResponse);
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

    private String buildUrl(Point startingPoint, List<Point> wayPoints, NavigationType navigationType) {

        String baseUrl;

        if (navigationType == NavigationType.STANDARD) {
            baseUrl = OSRM_ROUTE_STANDARD_URL;
        } else {
            baseUrl = OSRM_ROUTE_ELEVATION_URL;
        }

        return baseUrl + startingPoint.getX() + "," + startingPoint.getY() + ";" + wayPoints.stream()
                .map(point -> point.getX() + "," + point.getY())
                .collect(Collectors.joining(";")) + "?steps=true";

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


    private Map<String, Object> getElevationData(String polyline) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(OPENTOPODATA_URL).queryParam("locations", polyline);
        URI uri = builder.build().encode().toUri();

        try {
            return restTemplate.getForObject(uri, Map.class);
        } catch (RestClientException e) {
            if (e.getMessage().contains("Config Error: Dataset")) {
                throw new OpentopodataDatasetNotConfiguredException();
            } else if (e.getMessage().contains("Too many locations provided")) {
                throw new OpentopodataTooManyLocationsException();
            } else if (e.getMessage().contains("Connection refused")) {
                throw new OpentopodataConnectionRefusedException();
            } else {
                throw new GenericException();
            }
        }
    }

    private Map<String, Object> addElevation(Map<String, Object> osrmResponse, Map<String, Object> elevationData) {
        List<Map<String, Object>> osrmRoutes = (List<Map<String, Object>>) osrmResponse.get("routes");
        List<Map<String, Object>> elevationResults = (List<Map<String, Object>>) elevationData.get("results");

        for (Map<String, Object> route : osrmRoutes) {
            List<Map<String, Object>> legs = (List<Map<String, Object>>) route.get("legs");
            for (Map<String, Object> leg : legs) {
                List<Map<String, Object>> steps = (List<Map<String, Object>>) leg.get("steps");
                for (Map<String, Object> step : steps) {
                    Map<String, Object> maneuver = (Map<String, Object>) step.get("maneuver");
                    List<Double> location = (List<Double>) maneuver.get("location");
                    for (Map<String, Object> result : elevationResults) {
                        Map<String, Double> elevationLocation = (Map<String, Double>) result.get("location");
                        if (Math.round(location.get(0) * 10000) == Math.round(elevationLocation.get("lng") * 10000) &&
                                Math.round(location.get(1) * 10000) == Math.round(elevationLocation.get("lat") * 10000)) {
                            location.add((Double) result.get("elevation"));
                            break;
                        }
                    }
                }
            }
        }
        return osrmResponse;
    }


}



