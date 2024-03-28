package dev.redy1908.greenway.osrm.domain;

import java.util.List;

import org.locationtech.jts.geom.Point;

public interface IOsrmService {

    OsrmParsedData getParsedData(Point startPoint, List<Point> pointList);

}
