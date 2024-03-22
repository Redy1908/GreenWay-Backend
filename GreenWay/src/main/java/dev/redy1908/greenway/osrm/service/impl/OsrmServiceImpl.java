package dev.redy1908.greenway.osrm.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.LatLng;

import dev.redy1908.greenway.osrm.service.IOsrmService;
import dev.redy1908.greenway.point.Point;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OsrmServiceImpl implements IOsrmService {

    @Value("${osrm.driving_path}")
    private String OSRM_DRIVING_PATH;

    private final ObjectMapper objectMapper;

    private final RestTemplate restTemplate;

    @Override
    // TODO refactoring
    public String getRouting(Point startPoint, List<Point> destinations) {

        String url = OSRM_DRIVING_PATH + startPoint.longitude() + "," + startPoint.latitude() + ";";

        url += destinations.stream().map(
                point -> point.longitude() + "," + point.latitude())
                .collect(Collectors.joining(";"));

        url += "?overview=false&steps=true";

        String osrmResponse = restTemplate.getForObject(url, String.class);

        try {
            JsonNode responseJson = objectMapper.readTree(osrmResponse);

            List<String> polylineList = new ArrayList<>();
            JsonNode routes = responseJson.get("routes");

            if (routes.isArray()) {
                for (JsonNode route : routes) {
                    JsonNode legs = route.get("legs");
                    if (legs.isArray()) {
                        for (JsonNode leg : legs) {
                            JsonNode steps = leg.get("steps");
                            if (steps.isArray()) {
                                for (JsonNode step : steps) {
                                    String polyline = step.get("geometry").asText();
                                    polylineList.add(polyline);
                                }
                            }
                        }
                    }
                }
            }

            List<LatLng> allPoints = new ArrayList<>();

            for (String polyline : polylineList) {
                List<LatLng> deoded = PolylineEncoding.decode(polyline);
                allPoints.addAll(deoded);
            }

            String polyline = PolylineEncoding.encode(allPoints);
            System.out.println(polyline);
            return polyline;

        } catch (Exception e) {

        }

        return null;
    }
}
