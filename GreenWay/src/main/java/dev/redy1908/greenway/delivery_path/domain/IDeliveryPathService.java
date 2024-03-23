package dev.redy1908.greenway.delivery_path.domain;

import dev.redy1908.greenway.point.Point;

import java.util.List;

public interface IDeliveryPathService {

    DeliveryPath createDeliveryPath(Point startPoint, List<Point> pointList);
}
