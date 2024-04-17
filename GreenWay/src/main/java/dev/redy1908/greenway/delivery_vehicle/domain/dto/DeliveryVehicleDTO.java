package dev.redy1908.greenway.delivery_vehicle.domain.dto;

import dev.redy1908.greenway.delivery.domain.dto.DeliveryDTO;

import java.util.List;

public record DeliveryVehicleDTO(

        Long id,

        String modelName,

        int maxAutonomyKm,

        int maxCapacityKg,

        List<DeliveryDTO> deliveries
) {
}
