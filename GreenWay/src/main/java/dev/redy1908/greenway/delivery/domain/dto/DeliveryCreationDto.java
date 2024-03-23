package dev.redy1908.greenway.delivery.domain.dto;

import java.util.Set;

import org.locationtech.jts.geom.Point;

import dev.redy1908.greenway.delivery_package.domain.dto.DeliveryPackageDTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record DeliveryCreationDto(

                @NotNull Point startPoint,

                @NotNull Long vehicleId,

                @NotNull @NotEmpty Set<DeliveryPackageDTO> packages) {
}
