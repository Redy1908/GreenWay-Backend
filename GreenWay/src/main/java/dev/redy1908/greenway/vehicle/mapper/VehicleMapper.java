package dev.redy1908.greenway.vehicle.mapper;

import dev.redy1908.greenway.vehicle.dto.VehicleDTO;
import dev.redy1908.greenway.vehicle.model.Vehicle;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface VehicleMapper {

    VehicleDTO toDto(Vehicle vehicle);

    Vehicle toEntity(VehicleDTO vehicleDTO);
}
