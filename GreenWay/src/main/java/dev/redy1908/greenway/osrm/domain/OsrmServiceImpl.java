package dev.redy1908.greenway.osrm.domain;

import dev.redy1908.greenway.osrm.domain.exceptions.models.InvalidNavigationMode;
import dev.redy1908.greenway.osrm.domain.exceptions.models.InvalidOsrmResponseException;
import dev.redy1908.greenway.osrm.domain.exceptions.models.PointOutOfBoundsException;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Value;
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
    public NavigationData getNavigationData(Point startingPoint, Set<Point> wayPoints, String navigationType) {
        String url = buildUrl(startingPoint, wayPoints, navigationType);
        Map<String, Object> osrmResponse = restTemplate.getForObject(url, Map.class);

        if (osrmResponse == null) {
            throw new InvalidOsrmResponseException();
        }

        Map<String, Object> opentopodataResponse = getElevationData(extractPolyline(osrmResponse));

        return new NavigationData(opentopodataResponse, osrmResponse);
    }

    @Override
    public void checkPointBounds(Point point) {
        if (point.getX() < lonMin || point.getX() > lonMax || point.getY() < latMin || point.getY() > latMax) {
            throw new PointOutOfBoundsException(point, lonMin, lonMax, latMin, latMax);
        }
    }

    private String buildUrl(Point startingPoint, Set<Point> wayPoints, String navigationType) {

        String basePath;

        switch (navigationType) {
            case "distance" -> basePath = OSRM_DISTANCE_URL;
            case "duration" -> basePath = OSRM_DURATION_URL;
            case "elevation" -> basePath = OSRM_ELEVATION_URL;
            case "standard" -> basePath = OSRM_STANDARD_URL;
            default -> throw new InvalidNavigationMode();
        }

        return basePath + startingPoint.getX() + "," + startingPoint.getY() + ";" + wayPoints.stream()
                .map(point -> point.getX() + "," + point.getY())
                .collect(Collectors.joining(";"))
                + "?steps=true&source=first";
    }

    private String extractPolyline(Map<String, Object> osrmResponse) {

        if (osrmResponse.containsKey("trips")) {
            List<Object> trips = (List<Object>) osrmResponse.get("trips");
            if (!trips.isEmpty()) {
                Map<String, Object> firstRoute = (Map<String, Object>) trips.getFirst();
                if (firstRoute.containsKey("geometry")) {
                    return (String) firstRoute.get("geometry");
                }
            }
        }

        throw new InvalidOsrmResponseException();
    }

    private Map<String, Object> getElevationData(String polyline) {

        String urlEncodedPolyline = URLEncoder.encode(polyline, StandardCharsets.UTF_8);
        return restTemplate.getForObject(OPENTOPODATA_URL + urlEncodedPolyline, Map.class);
    }
}
