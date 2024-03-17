package dev.redy1908.greenway.delivery.dto;

import dev.redy1908.greenway.deliveryPackage.dto.DeliveryPackageDTO;
import dev.redy1908.greenway.deliveryPath.dto.DeliveryPathDTO;

import java.util.List;

public record DeliveryDTO(

        Long vehicleId,

        DeliveryPathDTO deliveryPath,

        List<DeliveryPackageDTO> deliveryPackages
) {
}
