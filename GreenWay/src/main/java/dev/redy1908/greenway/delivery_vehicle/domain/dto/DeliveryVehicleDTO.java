package dev.redy1908.greenway.delivery_vehicle.domain.dto;

import org.locationtech.jts.geom.Point;

public record DeliveryVehicleDTO(

        Long id,

        String modelName,

        int maxAutonomyKm,

        int maxCapacityKg,

        String depositAddress,

        Point depositCoordinates
) {
}
