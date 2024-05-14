package dev.redy1908.greenway.delivery.domain.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

public record DeliveryDTO(

        @NotNull
        int id,

        @NotEmpty
        String sender,

        @NotEmpty
        String senderAddress,

        @NotEmpty
        String receiver,

        @NotEmpty
        String receiverAddress,

        @NotNull
        Point receiverCoordinates,

        @NotNull
        LocalDateTime estimatedDeliveryTime,

        @NotNull
        Double weightKg,

        @Nullable
        Boolean inTransit,

        @Nullable
        LocalDateTime deliveryTime
) {
}