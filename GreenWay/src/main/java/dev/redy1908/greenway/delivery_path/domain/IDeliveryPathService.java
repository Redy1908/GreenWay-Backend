package dev.redy1908.greenway.delivery_path.domain;

import java.util.List;

import org.locationtech.jts.geom.Point;

public interface IDeliveryPathService {

    DeliveryPath createDeliveryPath(Point startPoint, List<Point> pointList);
}
