package dev.redy1908.greenway.osrm.domain;

import dev.redy1908.greenway.delivery.domain.Delivery;
import dev.redy1908.greenway.vehicle_deposit.domain.VehicleDeposit;
import org.locationtech.jts.geom.Point;
import org.springframework.data.util.Pair;

import java.util.List;

public interface IOsrmService {

   String getNavigationData(List<Point> wayPoints, NavigationType navigationType);

    void checkPointInBounds(Point point);

    Pair<double[][], double[][]> getMatrixDistances(VehicleDeposit vehicleDeposit, List<Delivery> deliveryList);
}
