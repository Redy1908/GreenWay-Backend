package dev.redy1908.greenway.delivery.service.impl;

import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.LatLng;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import dev.redy1908.greenway.delivery.dto.DeliveryCreationDto;
import dev.redy1908.greenway.delivery.model.Delivery;
import dev.redy1908.greenway.delivery.service.IDeliveryService;
import dev.redy1908.greenway.deliveryMan.model.DeliveryMan;
import dev.redy1908.greenway.deliveryPath.model.DeliveryPath;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements IDeliveryService {

    private final RestTemplate restTemplate;

    @Override
    public Delivery createDelivery(DeliveryCreationDto deliveryCreationDto) {

        Point start = deliveryCreationDto.startPoint();
        Point end = deliveryCreationDto.endPoint();

        String osrmResponse = getRouting(start, end);
        String encodedPolyline = extractEncodedPolyline(osrmResponse);
        LineString decodedPolyline = decodePolylineToLineString(encodedPolyline);
        Double distance = extractDistance(osrmResponse);
        Double duration = extractDuration(osrmResponse);

        DeliveryPath deliveryPath = new DeliveryPath(
                deliveryCreationDto.startPoint(),
                deliveryCreationDto.endPoint(),
                distance,
                duration,
                encodedPolyline,
                decodedPolyline
        );

        return new Delivery(
                new DeliveryMan(),
                deliveryPath
        );
    }

    private String getRouting(Point start, Point end){
        String url = "http://green-way-osrm:5000//route/v1/driving/" + start.getX() + "," + start.getY() + ";" + end.getX() + "," + end.getY();
        return restTemplate.getForEntity(url, String.class).getBody();
    }

    private String extractEncodedPolyline(String osrmResponse){
        JSONObject jsonObject = new JSONObject(osrmResponse);
        JSONObject route = jsonObject.getJSONArray("routes").getJSONObject(0);
        return route.getString("geometry");
    }

    private Double extractDistance(String osrmResponse){
        JSONObject jsonObject = new JSONObject(osrmResponse);
        JSONObject route = jsonObject.getJSONArray("routes").getJSONObject(0);
        return route.getDouble("distance");
    }

    private Double extractDuration(String osrmResponse){
        JSONObject jsonObject = new JSONObject(osrmResponse);
        JSONObject route = jsonObject.getJSONArray("routes").getJSONObject(0);
        return route.getDouble("duration");
    }

    private LineString decodePolylineToLineString(String encodedPolyline){
        List<LatLng> latLngs = PolylineEncoding.decode(encodedPolyline);


       Coordinate[] coordinates = latLngs.stream()
                .map(latlng -> new Coordinate(latlng.lng, latlng.lat))
                .toArray(Coordinate[]::new);

        return new GeometryFactory().createLineString(coordinates);
    }
}
