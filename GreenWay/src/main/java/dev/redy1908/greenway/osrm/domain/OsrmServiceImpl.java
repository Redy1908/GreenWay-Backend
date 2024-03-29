package dev.redy1908.greenway.osrm.domain;

import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.LatLng;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class OsrmServiceImpl implements IOsrmService {

    @Value("${osrm.driving_path}")
    private String OSRM_DRIVING_PATH;

    private final RestTemplate restTemplate;

    @Override
    public NavigationData getNavigationData(Point startPoint, Set<Point> destinations) {

        String url = OSRM_DRIVING_PATH + startPoint.getX() + "," + startPoint.getY() + ";";

        url += destinations.stream().map(
                        point -> point.getX() + "," + point.getY())
                .collect(Collectors.joining(";"));

        url += "?overview=false&steps=true";

        Map<String, Object> osrmResponse = restTemplate.getForObject(url, Map.class);

        return new NavigationData(osrmResponse);

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

    private String generateSinglePolyline(String osrmResponse) {
        JSONObject responseJson = new JSONObject(osrmResponse);
        JSONArray routes = responseJson.getJSONArray("routes");

        List<LatLng> allPoints = new ArrayList<>();

        for (Object r : routes) {
            JSONObject route = (JSONObject) r;
            JSONArray legs = route.getJSONArray("legs");

            for (Object l : legs) {
                JSONObject leg = (JSONObject) l;
                JSONArray steps = leg.getJSONArray("steps");

                for (Object s : steps) {
                    JSONObject step = (JSONObject) s;
                    String geometry = step.getString("geometry");
                    List<LatLng> decodedPath = PolylineEncoding.decode(geometry);
                    allPoints.addAll(decodedPath);
                }
            }
        }

        return PolylineEncoding.encode(allPoints);
    }
}
