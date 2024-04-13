package dev.redy1908.greenway.osrm.domain;

import org.locationtech.jts.geom.Point;

import java.util.Map;
import java.util.Set;

public interface IOsrmService {

    Map<String, Object> getNavigationData(Point startingPoint, double maxDistance, Set<Point> wayPoints, NavigationType navigationType);

    Map<String, Object> getElevationData(Point startingPoint, double maxDistance, Set<Point> wayPoints, NavigationType navigationType);

    void checkPointBounds(Point point);

    double getTripLength(Point startingPoint, Set<Point> wayPoints, NavigationType navigationType, RequestType requestType);
}
