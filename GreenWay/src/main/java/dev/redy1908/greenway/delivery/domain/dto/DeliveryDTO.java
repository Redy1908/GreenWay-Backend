package dev.redy1908.greenway.delivery.domain.dto;

import dev.redy1908.greenway.delivery_package.domain.dto.DeliveryPackageDTO;
import jakarta.validation.constraints.NotNull;
import org.locationtech.jts.geom.Point;

import javax.annotation.Nullable;
import java.util.Set;

public record DeliveryDTO(

        @Nullable Long deliveryId,

        @Nullable String deliveryManUsername,

        @NotNull Long vehicleId,

        @NotNull String depositAddress,

        @NotNull Point depositCoordinates,

        @NotNull Set<DeliveryPackageDTO> deliveryPackages) {
}
