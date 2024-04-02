package dev.redy1908.greenway.osrm.domain;

import dev.redy1908.greenway.osrm.domain.exceptions.models.InvalidOsrmResponseException;
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

    private final RestTemplate restTemplate;

    @Override
    public NavigationData getNavigationDataDistance(Point startPoint, Set<Point> pointList) {

        String url = buildUrl(OSRM_DISTANCE_URL, startPoint, pointList);
        return getNavigationData(url);
    }

    @Override
    public NavigationData getNavigationDataDuration(Point startPoint, Set<Point> pointList) {
        String url = buildUrl(OSRM_DURATION_URL, startPoint, pointList);
        return getNavigationData(url);
    }

    @Override
    public NavigationData getNavigationDataElevation(Point startPoint, Set<Point> pointList) {
        String url = buildUrl(OSRM_ELEVATION_URL, startPoint, pointList);
        return getNavigationData(url);
    }

    @Override
    public NavigationData getNavigationDataStandard(Point startPoint, Set<Point> pointList) {
        String url = buildUrl(OSRM_STANDARD_URL, startPoint, pointList);
        return getNavigationData(url);
    }

    @Override
    public NavigationData getNavigationData(String url) {
        Map<String, Object> osrmResponse = restTemplate.getForObject(url, Map.class);

        if (osrmResponse == null) {
            throw new InvalidOsrmResponseException();
        }

        Map<String, Object> opentopodataResponse = getElevationData(extractPolyline(osrmResponse));

        return new NavigationData(opentopodataResponse, osrmResponse);
    }

    private String buildUrl(String basePath, Point startingPoint, Set<Point> wayPoints) {
        return basePath + startingPoint.getX() + "," + startingPoint.getY() + ";"

                + wayPoints.stream().map(
                        point -> point.getX() + "," + point.getY())
                .collect(Collectors.joining(";"))

                + "?steps=true";
    }

    private String extractPolyline(Map<String, Object> osrmResponse) {

        if (osrmResponse.containsKey("routes")) {
            List<Object> routes = (List<Object>) osrmResponse.get("routes");
            if (!routes.isEmpty()) {
                Map<String, Object> firstRoute = (Map<String, Object>) routes.getFirst();
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
