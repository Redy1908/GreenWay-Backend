package dev.redy1908.greenway.osrm.domain;

import org.locationtech.jts.geom.Point;

import java.util.Set;

public interface IOsrmService {

    NavigationData getNavigationDataDistance(Point startingPoint, Set<Point> wayPoints);
    NavigationData getNavigationDataDuration(Point startingPoint, Set<Point> wayPoints);
    NavigationData getNavigationDataElevation(Point startingPoint, Set<Point> wayPoints);
    NavigationData getNavigationDataStandard(Point startingPoint, Set<Point> wayPoints);

    NavigationData getNavigationData(String url);


}
