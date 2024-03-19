package dev.redy1908.greenway.delivery_path.service;

import dev.redy1908.greenway.delivery_path.model.DeliveryPath;
import dev.redy1908.greenway.point.Point;

import java.util.List;

public interface IDeliveryPathService {

    DeliveryPath createDeliveryPath(Point startPoint, List<Point> pointList);
}
