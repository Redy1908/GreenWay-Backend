package dev.redy1908.greenway.osrm.domain;

import dev.redy1908.greenway.delivery.domain.Delivery;
import dev.redy1908.greenway.delivery_vehicle.domain.DeliveryVehicle;
import kotlin.Pair;
import org.locationtech.jts.geom.Point;

import java.util.List;
import java.util.Map;

public interface IOsrmService {

    Map<String, Object> getNavigationData(Point startingPoint, List<Point> wayPoints);

    Map<String, Object> getElevationData(Point startingPoint, List<Point> wayPoints);

    void checkPointInBounds(Point point);

    Pair<double[][], double[][]> getMatrixDistances(List<DeliveryVehicle> deliveryVehicleList, List<Delivery> deliveryList);
}
