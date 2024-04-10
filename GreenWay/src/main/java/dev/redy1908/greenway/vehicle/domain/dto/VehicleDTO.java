package dev.redy1908.greenway.vehicle.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record VehicleDTO(
        @NotEmpty String modelName,

        @NotNull Double maxAutonomyKm,

        @NotNull Double maxCapacityKg) {
}
