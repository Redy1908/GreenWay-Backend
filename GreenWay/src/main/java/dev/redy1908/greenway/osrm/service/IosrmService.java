package dev.redy1908.greenway.osrm.service;

import dev.redy1908.greenway.point.Point;
import org.json.JSONObject;

import java.util.List;

public interface IOsrmService {

    String getRouting(Point startPoint, List<Point> pointList);

    default Double extractDuration(String jsonOsrmResponse) {
        JSONObject jsonObject = new JSONObject(jsonOsrmResponse);
        JSONObject route = jsonObject.getJSONArray("routes").getJSONObject(0);
        return route.getDouble("duration");
    }

    default Double extractDistance(String jsonOsrmResponse) {
        JSONObject jsonObject = new JSONObject(jsonOsrmResponse);
        JSONObject route = jsonObject.getJSONArray("routes").getJSONObject(0);
        return route.getDouble("distance");
    }

    default String extractPolyline(String jsonOsrmResponse) {
        JSONObject jsonObject = new JSONObject(jsonOsrmResponse);
        JSONObject route = jsonObject.getJSONArray("routes").getJSONObject(0);
        return route.getString("geometry");
    }

}
