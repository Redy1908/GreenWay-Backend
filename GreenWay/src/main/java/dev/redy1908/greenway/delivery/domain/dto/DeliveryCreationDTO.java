package dev.redy1908.greenway.delivery.domain.dto;

import jakarta.validation.constraints.NotNull;
import org.locationtech.jts.geom.Point;

public record DeliveryCreationDTO(

        @NotNull String sender,

        @NotNull String senderAddress,

        @NotNull String receiver,

        @NotNull String receiverAddress,

        @NotNull Point receiverCoordinates,

        @NotNull Double weightKg

) {

}
