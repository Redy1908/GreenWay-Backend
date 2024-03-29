package dev.redy1908.greenway.osrm.domain;

import org.locationtech.jts.geom.Point;

import java.util.Set;

public interface IOsrmService {

    NavigationData getNavigationData(Point startPoint, Set<Point> pointList);

}
