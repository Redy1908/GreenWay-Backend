package dev.redy1908.greenway.vehicle.domain.dto;

public record VehicleDTO(
                Long id,
                String model,
                Double batteryNominalCapacity,
                Double vehicleConsumption,
                Double currentBatteryCharge,

                Double maxCapacity) {
}
