package dev.redy1908.greenway.deliveryPath.dto;

public record DeliveryPathDTO(
        Double distanceInMeters,
        Double durationInSeconds,
        String polyline
) {
}
