package dev.redy1908.greenway.vehicle.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record VehicleDTO(
        @NotEmpty String model,

        @NotNull Double batteryNominalCapacity,

        @NotNull Double vehicleConsumption,

        @NotNull Double currentBatteryCharge,

        @NotNull Double maxCapacity) {
}
