package dev.redy1908.greenway.delivery.dto;

import dev.redy1908.greenway.delivery_package.dto.DeliveryPackageDTO;
import dev.redy1908.greenway.delivery_path.dto.DeliveryPathDTO;

import java.util.List;

public record DeliveryDTO(

        Long vehicleId,

        DeliveryPathDTO deliveryPath,

        List<DeliveryPackageDTO> deliveryPackages
) {
}
