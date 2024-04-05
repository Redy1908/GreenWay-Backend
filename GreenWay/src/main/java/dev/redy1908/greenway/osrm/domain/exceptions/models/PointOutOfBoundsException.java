package dev.redy1908.greenway.osrm.domain.exceptions.models;

import org.locationtech.jts.geom.Point;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class PointOutOfBoundsException extends RuntimeException {

    public PointOutOfBoundsException(Point point, double lonMin, double lonMax, double latMin, double latMax) {
        super(String.format("Point (longitude: %f, latitude: %f) is out of bounds. The valid range is Longitude: %f to %f, Latitude: %f to %f",
                point.getX(), point.getY(), lonMin, lonMax, latMin, latMax));
    }
}
