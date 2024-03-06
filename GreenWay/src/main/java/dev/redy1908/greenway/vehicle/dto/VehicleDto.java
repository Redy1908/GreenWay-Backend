package dev.redy1908.greenway.vehicle.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record VehicleDto(

        @NotEmpty
        String model,
        @NotNull
        Double batteryNominalCapacity,

        @NotNull
        Double vehicleConsumption,
        @NotNull
        String chargePortType,

        @NotNull
        Double chargingPower
) {
}
