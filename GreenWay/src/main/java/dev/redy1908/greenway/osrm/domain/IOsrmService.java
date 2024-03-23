package dev.redy1908.greenway.osrm.domain;

import dev.redy1908.greenway.point.Point;

import java.util.List;

public interface IOsrmService {

    OsrmParsedData getParsedData(Point startPoint, List<Point> pointList);

}
