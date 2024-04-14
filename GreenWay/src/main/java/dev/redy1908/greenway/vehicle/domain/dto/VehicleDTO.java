package dev.redy1908.greenway.vehicle.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import javax.annotation.Nullable;

public record VehicleDTO(

        @Nullable Long id,

        @NotEmpty String modelName,

        @NotNull Double maxAutonomyKm,

        @NotNull Double maxCapacityKg) {
}
