package dev.redy1908.greenway.osrm.domain;

import dev.redy1908.greenway.delivery_vehicle.domain.DeliveryVehicle;
import dev.redy1908.greenway.delivery.domain.Delivery;
import dev.redy1908.greenway.osrm.domain.exceptions.models.InvalidNavigationMode;
import dev.redy1908.greenway.osrm.domain.exceptions.models.InvalidOsrmResponseException;
import dev.redy1908.greenway.osrm.domain.exceptions.models.PointOutOfBoundsException;
import dev.redy1908.greenway.delivery_vehicle.domain.exceptions.models.VehicleAutonomyNotSufficientException;
import kotlin.Pair;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
class OsrmServiceImpl implements IOsrmService {

    @Value("${osrm.distance.url}")
    private String OSRM_DISTANCE_URL;

    @Value("${osrm.duration.url}")
    private String OSRM_DURATION_URL;

    @Value("${osrm.elevation.url}")
    private String OSRM_ELEVATION_URL;

    @Value("${osrm.standard.url}")
    private String OSRM_STANDARD_URL;

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
    public Map<String, Object> getNavigationData(Point startingPoint, double maxDistance, Set<Point> wayPoints, NavigationType navigationType) {
        return getOsrmResponse(startingPoint, maxDistance, wayPoints, navigationType, RequestType.FULL);
    }


    @Override
    public Map<String, Object> getElevationData(Point startingPoint, double maxDistance, Set<Point> wayPoints, NavigationType navigationType) {
        Map<String, Object> osrmResponse = getOsrmResponse(startingPoint, maxDistance, wayPoints, navigationType, RequestType.OVERVIEW);

        String urlEncodedPolyline = URLEncoder.encode(extractAttributeFromTrip(osrmResponse, "geometry"), StandardCharsets.UTF_8);
        return restTemplate.getForObject(OPENTOPODATA_URL + urlEncodedPolyline, Map.class);
    }

    private Map<String, Object> getOsrmResponse(Point startingPoint, double maxDistance, Set<Point> wayPoints, NavigationType navigationType, RequestType requestType) {
        String url = buildUrl(startingPoint, wayPoints, navigationType, requestType);
        Map<String, Object> osrmResponse = restTemplate.getForObject(url, Map.class);

        if (osrmResponse == null) {
            throw new InvalidOsrmResponseException();
        }

        double tripDistance = Double.parseDouble(extractAttributeFromTrip(osrmResponse, "distance")) / 1000;

        if(tripDistance > maxDistance) {
            throw new VehicleAutonomyNotSufficientException(maxDistance, tripDistance);
        }

        return osrmResponse;
    }

    @Override
    public void checkPointsInBounds(List<Point> points) {

        points.forEach(point -> {
            if(point.getX() < lonMin || point.getX() > lonMax || point.getY() < latMin || point.getY() > latMax){
                throw new PointOutOfBoundsException(point, lonMin, lonMax, latMin, latMax);
            }
        });
    }

    @Override
    public double getTripLength(Point startingPoint, Set<Point> wayPoints, NavigationType navigationType, RequestType requestType) {
        String url = buildUrl(startingPoint, wayPoints, navigationType, requestType);
        Map<String, Object> osrmResponse = restTemplate.getForObject(url, Map.class);

        if (osrmResponse == null) {
            throw new InvalidOsrmResponseException();
        }

        return Double.parseDouble(extractAttributeFromTrip(osrmResponse, "distance"));

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

        return new Pair<>(matrixDurations, matrixDistances);
    }

    private String buildUrlMatrix(List<DeliveryVehicle> deliveryVehicleList, List<Delivery> deliveryList){
        StringBuilder urlBuilder = new StringBuilder("http://localhost:5000/table/v1/driving/");

        for(DeliveryVehicle deliveryVehicle: deliveryVehicleList) {
            urlBuilder.append(deliveryVehicle.getDepositCoordinates().getX());
            urlBuilder.append(",");
            urlBuilder.append(deliveryVehicle.getDepositCoordinates().getY());
            urlBuilder.append(";");
        }

        for (Delivery delivery : deliveryList) {
            urlBuilder.append(delivery.getReceiverCoordinates().getX());
            urlBuilder.append(",");
            urlBuilder.append(delivery.getReceiverCoordinates().getY());
            urlBuilder.append(";");
        }

        urlBuilder.deleteCharAt(urlBuilder.length() - 1);

        /*urlBuilder.append("?sources=0");

        urlBuilder.append("&destinations=");
        for (int i = 1; i <= deliveryList.size(); i++) {
            urlBuilder.append(i);
            urlBuilder.append(";");
        }

        urlBuilder.deleteCharAt(urlBuilder.length() - 1);

        urlBuilder.append("&annotations=duration,distance");*/

        urlBuilder.append("?annotations=duration,distance");

        return urlBuilder.toString();
    }

    private String buildUrl(Point startingPoint, Set<Point> wayPoints, NavigationType navigationType, RequestType requestType) {

        String basePath;

        switch (navigationType) {
            case NavigationType.DISTANCE -> basePath = OSRM_DISTANCE_URL;
            case NavigationType.DURATION -> basePath = OSRM_DURATION_URL;
            case NavigationType.ELEVATION -> basePath = OSRM_ELEVATION_URL;
            case NavigationType.STANDARD -> basePath = OSRM_STANDARD_URL;
            default -> throw new InvalidNavigationMode();
        }

        String url = basePath + startingPoint.getX() + "," + startingPoint.getY() + ";" + wayPoints.stream()
                .map(point -> point.getX() + "," + point.getY())
                .collect(Collectors.joining(";"))
                + "?source=first";

        if (requestType == RequestType.FULL ) {
            url += "&steps=true&overview=false";
        }

        return url;
    }

    private String extractAttributeFromTrip(Map<String, Object> osrmResponse, String attributeName) {

        if (osrmResponse.containsKey("trips")) {
            List<Object> trips = (List<Object>) osrmResponse.get("trips");
            if (!trips.isEmpty()) {
                Map<String, Object> firstRoute = (Map<String, Object>) trips.getFirst();
                if (firstRoute.containsKey(attributeName)) {
                    return String.valueOf(firstRoute.get(attributeName));
                }
            }
        }

        throw new InvalidOsrmResponseException();
    }
}
