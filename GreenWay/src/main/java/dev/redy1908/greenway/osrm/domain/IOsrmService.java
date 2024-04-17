package dev.redy1908.greenway.osrm.domain;

import dev.redy1908.greenway.delivery.domain.Delivery;
import dev.redy1908.greenway.vehicle_deposit.domain.VehicleDeposit;
import org.locationtech.jts.geom.Point;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.Map;

public interface IOsrmService {

    Map<String, Object> getNavigationData(Point startingPoint, List<Point> wayPoints);

    Map<String, Object> getElevationData(Point startingPoint, List<Point> wayPoints);

    void checkPointInBounds(Point point);

    Pair<double[][], double[][]> getMatrixDistances(VehicleDeposit vehicleDeposit, List<Delivery> deliveryList);
}
