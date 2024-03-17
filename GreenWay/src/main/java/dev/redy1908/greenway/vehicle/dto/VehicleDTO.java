package dev.redy1908.greenway.vehicle.dto;

public record VehicleDTO(
        Long id,
        String model,
        Double batteryNominalCapacity,
        Double vehicleConsumption,
        Double currentBatteryCharge
) {
}
