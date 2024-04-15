package dev.redy1908.greenway.osrm.domain;

import dev.redy1908.greenway.delivery.domain.Delivery;
import dev.redy1908.greenway.delivery_vehicle.domain.DeliveryVehicle;
import kotlin.Pair;
import org.locationtech.jts.geom.Point;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IOsrmService {

    Map<String, Object> getNavigationData(Point startingPoint, double maxDistance, Set<Point> wayPoints, NavigationType navigationType);

    Map<String, Object> getElevationData(Point startingPoint, double maxDistance, Set<Point> wayPoints, NavigationType navigationType);

    void checkPointsInBounds(List<Point> points);

    double getTripLength(Point startingPoint, Set<Point> wayPoints, NavigationType navigationType, RequestType requestType);

    Pair<double[][], double[][]> getMatrixDistances(List<DeliveryVehicle> deliveryVehicleList, List<Delivery> deliveryList);
}
