package dev.redy1908.greenway.delivery_package.domain.dto;

import org.locationtech.jts.geom.Point;

public record DeliveryPackageDTO(
                Point destination,
                Double weight) {
}
