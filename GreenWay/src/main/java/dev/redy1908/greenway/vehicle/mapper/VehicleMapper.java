package dev.redy1908.greenway.vehicle.mapper;

import dev.redy1908.greenway.vehicle.dto.VehicleDTO;
import dev.redy1908.greenway.vehicle.model.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VehicleMapper {

    VehicleDTO toDto(Vehicle vehicle);

    Vehicle toEntity(VehicleDTO vehicleDTO);
}
