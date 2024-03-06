package dev.redy1908.greenway.delivery.dto;

import com.vividsolutions.jts.geom.Point;
import jakarta.validation.constraints.NotNull;

public record DeliveryCreationDto(
        @NotNull
        Long deliveryMan,
        @NotNull
        Point startPoint,
        @NotNull
        Point endPoint
) {}
