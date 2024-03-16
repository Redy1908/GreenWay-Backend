package dev.redy1908.greenway.deliveryPath.service;

import dev.redy1908.greenway.deliveryPath.model.DeliveryPath;
import dev.redy1908.greenway.point.Point;

import java.util.List;

public interface IDeliveryPathService {

    DeliveryPath createDeliveryPath(Point startPoint, List<Point> pointList);
}
