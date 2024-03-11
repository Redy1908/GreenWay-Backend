package dev.redy1908.greenway.vehicle.service;

import dev.redy1908.greenway.vehicle.dto.VehicleDto;
import dev.redy1908.greenway.vehicle.dto.VehiclePageResponseDTO;

public interface IVehicleService {

    void saveVehicle(VehicleDto vehicleDto);

    VehicleDto getVehicleById(Long vehicleId);

    VehiclePageResponseDTO getFreeVehicles(int pageNo, int pageSize);

    VehiclePageResponseDTO getAllVehicles(int pageNo, int pageSize);
}
