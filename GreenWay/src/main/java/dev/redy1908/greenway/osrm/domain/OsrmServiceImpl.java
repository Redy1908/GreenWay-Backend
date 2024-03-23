package dev.redy1908.greenway.osrm.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.LatLng;

import dev.redy1908.greenway.point.Point;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
class OsrmServiceImpl implements IOsrmService {

    @Value("${osrm.driving_path}")
    private String OSRM_DRIVING_PATH;

    private final RestTemplate restTemplate;

    @Override
    public OsrmParsedData getParsedData(Point startPoint, List<Point> destinations) {

        String url = OSRM_DRIVING_PATH + startPoint.longitude() + "," + startPoint.latitude() + ";";

        url += destinations.stream().map(
                point -> point.longitude() + "," + point.latitude())
                .collect(Collectors.joining(";"));

        url += "?overview=false&steps=true";

        String osrmResponse = restTemplate.getForObject(url, String.class);

        List<String> polylineList = extractPolylines(osrmResponse);
        return new OsrmParsedData(extractDistance(osrmResponse), extractDuration(osrmResponse),
                joinPolylines(polylineList));

    }

    private Double extractDuration(String jsonOsrmResponse) {
        JSONObject jsonObject = new JSONObject(jsonOsrmResponse);
        JSONObject route = jsonObject.getJSONArray("routes").getJSONObject(0);
        return route.getDouble("duration");
    }

    private Double extractDistance(String jsonOsrmResponse) {
        JSONObject jsonObject = new JSONObject(jsonOsrmResponse);
        JSONObject route = jsonObject.getJSONArray("routes").getJSONObject(0);
        return route.getDouble("distance");
    }

    private List<String> extractPolylines(String osrmResponse) {
        JSONObject responseJson = new JSONObject(osrmResponse);
        List<String> polylineList = new ArrayList<>();
        JSONArray routes = responseJson.getJSONArray("routes");

        for (int i = 0; i < routes.length(); i++) {
            JSONObject route = routes.getJSONObject(i);
            JSONArray legs = route.getJSONArray("legs");

            for (int j = 0; j < legs.length(); j++) {
                JSONObject leg = legs.getJSONObject(j);
                JSONArray steps = leg.getJSONArray("steps");

                for (int k = 0; k < steps.length(); k++) {
                    JSONObject step = steps.getJSONObject(k);
                    String polyline = step.getString("geometry");
                    polylineList.add(polyline);
                }
            }
        }

        return polylineList;
    }

    private String joinPolylines(List<String> polylineList) {
        List<LatLng> allPoints = new ArrayList<>();

        for (String polyline : polylineList) {
            List<LatLng> deoded = PolylineEncoding.decode(polyline);
            allPoints.addAll(deoded);
        }

        return PolylineEncoding.encode(allPoints);
    }
}
