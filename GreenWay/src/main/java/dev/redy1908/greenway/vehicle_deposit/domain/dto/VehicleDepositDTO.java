package dev.redy1908.greenway.vehicle_deposit.domain.dto;

import jakarta.validation.constraints.NotNull;
import org.locationtech.jts.geom.Point;

public record VehicleDepositDTO(
        @NotNull String depositAddress,

        @NotNull Point depositCoordinates
) {
}
