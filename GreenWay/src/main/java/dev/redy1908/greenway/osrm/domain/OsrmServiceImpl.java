package dev.redy1908.greenway.osrm.domain;

import dev.redy1908.greenway.osrm.domain.exceptions.models.InvalidOsrmResponseException;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
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

        if(osrmResponse == null){
            throw new InvalidOsrmResponseException();
        }

        Map<String, Object> opentopodataResponse = getElevationData(extractPolyline(osrmResponse));

        return new NavigationData(opentopodataResponse, osrmResponse);

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
