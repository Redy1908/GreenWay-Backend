package dev.redy1908.greenway.osrm.domain;

import org.locationtech.jts.geom.Point;

import java.util.Set;

public interface IOsrmService {

    NavigationData getNavigationData(Point startingPoint, Set<Point> wayPoints, String navigationType);
    void checkPointBounds(Point point);
}
