package dev.redy1908.greenway.delivery_vehicle.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record DeliveryVehicleCreationDTO(

        @NotEmpty String modelName,

        @NotNull Double maxAutonomyKm,

        @NotNull Double maxCapacityKg
) {
}