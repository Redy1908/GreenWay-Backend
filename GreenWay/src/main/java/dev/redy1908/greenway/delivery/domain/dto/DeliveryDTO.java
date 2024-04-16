package dev.redy1908.greenway.delivery.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.locationtech.jts.geom.Point;

public record DeliveryDTO(

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
        Double weightKg
) {
}
