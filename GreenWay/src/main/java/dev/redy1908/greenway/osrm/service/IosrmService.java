package dev.redy1908.greenway.osrm.service;

import dev.redy1908.greenway.osrm.model.OsrmParsedData;
import dev.redy1908.greenway.point.Point;

import java.util.List;

public interface IOsrmService {

    OsrmParsedData getParsedData(Point startPoint, List<Point> pointList);

}
