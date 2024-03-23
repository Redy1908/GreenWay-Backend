package dev.redy1908.greenway.delivery.domain.dto;

import dev.redy1908.greenway.delivery_package.domain.dto.DeliveryPackageDTO;
import dev.redy1908.greenway.delivery_path.domain.dto.DeliveryPathDTO;

import java.util.List;

public record DeliveryDTO(

                Long vehicleId,

                DeliveryPathDTO deliveryPath,

                List<DeliveryPackageDTO> deliveryPackages) {
}
