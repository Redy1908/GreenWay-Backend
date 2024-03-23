package dev.redy1908.greenway.delivery.domain.dto;

import dev.redy1908.greenway.delivery_package.domain.dto.DeliveryPackageDTO;
import dev.redy1908.greenway.point.Point;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record DeliveryCreationDto(

                @NotNull Point startPoint,

                @NotNull Long vehicleId,

                @NotNull @NotEmpty Set<DeliveryPackageDTO> packages) {
}