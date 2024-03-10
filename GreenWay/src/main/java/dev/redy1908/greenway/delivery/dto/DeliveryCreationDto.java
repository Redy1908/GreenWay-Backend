package dev.redy1908.greenway.delivery.dto;

import dev.redy1908.greenway.point.Point;
import jakarta.validation.constraints.NotNull;

public record DeliveryCreationDto(
        @NotNull
        String deliveryManUsername,
        @NotNull
        Point startPoint,
        @NotNull
        Point endPoint
) {}
