package dev.redy1908.greenway.delivery_path.dto;

public record DeliveryPathDTO(
        Double distanceInMeters,
        Double durationInSeconds,
        String polyline
) {
}
