package dev.redy1908.greenway.osrm.domain;

import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.LatLng;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class OsrmServiceImpl implements IOsrmService {

    @Value("${osrm.url}")
    private String OSRM_URL;

    @Value("${opentopodata.url}")
    private String OPENTOPODATA_URL;

    private final RestTemplate restTemplate;

    @Override
    public NavigationData getNavigationData(Point startPoint, Set<Point> destinations) {

        String url = OSRM_URL + startPoint.getX() + "," + startPoint.getY() + ";";

        url += destinations.stream().map(
                        point -> point.getX() + "," + point.getY())
                .collect(Collectors.joining(";"));

        url += "?steps=true";

        Map<String, Object> osrmResponse = restTemplate.getForObject(url, Map.class);
        Map<String, Object> opentopodataResponse = getElevationData(extractOverviewPoints(osrmResponse));

        return new NavigationData(opentopodataResponse, osrmResponse);

    }

    private List<LatLng> extractOverviewPoints(Map<String, Object> osrmResponse) {

        if (osrmResponse.containsKey("routes")) {
            List<Object> routes = (List<Object>) osrmResponse.get("routes");
            if (!routes.isEmpty()) {
                Map<String, Object> firstRoute = (Map<String, Object>) routes.getFirst();
                if (firstRoute.containsKey("geometry")) {
                    String geometry = (String) firstRoute.get("geometry");
                    return PolylineEncoding.decode(geometry);
                }
            }
        }

        return null;
    }

    private Map<String, Object> getElevationData(List<LatLng> points) {
        String url = OPENTOPODATA_URL + points.stream().map(
                        point -> point.lat + "," + point.lng)
                .collect(Collectors.joining("|"));

        return restTemplate.getForObject(url, Map.class);
    }
}
