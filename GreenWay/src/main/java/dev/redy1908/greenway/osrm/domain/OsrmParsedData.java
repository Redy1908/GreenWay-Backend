package dev.redy1908.greenway.osrm.domain;

public record OsrmParsedData(
                Double distanceInMeters,
                Double durationInSeconds,
                String polyline) {

}
