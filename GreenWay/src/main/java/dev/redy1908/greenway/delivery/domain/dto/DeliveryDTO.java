package dev.redy1908.greenway.delivery.domain.dto;

import dev.redy1908.greenway.delivery_package.domain.dto.DeliveryPackageDTO;
import org.locationtech.jts.geom.Point;

import java.util.List;

public record DeliveryDTO(

        Long vehicleId,

        String polyline,

        Point startingPoint,

        List<DeliveryPackageDTO> deliveryPackages) {
}
