package dev.redy1908.greenway.vehicle.domain;

import dev.redy1908.greenway.vehicle.domain.dto.VehicleDTO;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VehicleMapper {

    VehicleDTO toDto(Vehicle vehicle);

    Vehicle toEntity(VehicleDTO vehicleDTO);
}
