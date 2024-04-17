package dev.redy1908.greenway.delivery_vehicle.domain.dto;

public record DeliveryVehicleNoDeliveriesDTO(

        String modelName,

        int maxAutonomyKm,

        int maxCapacityKg
) {
}