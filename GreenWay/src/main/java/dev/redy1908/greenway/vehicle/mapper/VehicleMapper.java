package dev.redy1908.greenway.vehicle.mapper;

import dev.redy1908.greenway.vehicle.dto.VehicleDto;
import dev.redy1908.greenway.vehicle.model.Vehicle;
import org.springframework.stereotype.Component;
import org.mapstruct.Mapper;

@Component
@Mapper
public interface VehicleMapper {

    VehicleDto toDto(Vehicle vehicle);
    Vehicle toEntity(VehicleDto vehicleDto);
}
