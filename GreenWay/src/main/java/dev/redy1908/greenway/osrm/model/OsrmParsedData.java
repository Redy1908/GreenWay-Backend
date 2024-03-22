package dev.redy1908.greenway.osrm.model;

public record OsrmParsedData(
        Double distanceInMeters,
        Double durationInSeconds,
        String polyline) {

}
