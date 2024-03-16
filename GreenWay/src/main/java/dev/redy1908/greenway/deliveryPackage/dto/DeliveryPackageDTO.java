package dev.redy1908.greenway.deliveryPackage.dto;

import dev.redy1908.greenway.point.Point;

public record DeliveryPackageDTO (
        Point destination,

        Double weight
){
}
