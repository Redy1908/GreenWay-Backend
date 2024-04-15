package dev.redy1908.greenway.delivery_vehicle.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.locationtech.jts.geom.Point;

public record DeliveryVehicleCreationDTO(

        @NotEmpty String modelName,

        @NotNull Double maxAutonomyKm,

        @NotNull Double maxCapacityKg,

        @NotNull String depositAddress,

        @NotNull Point depositCoordinates
) {
}