package dev.redy1908.greenway.vehicle.service;

import dev.redy1908.greenway.vehicle.dto.VehicleCreationDTO;
import dev.redy1908.greenway.vehicle.dto.VehiclePageResponseDTO;
import dev.redy1908.greenway.vehicle.model.Vehicle;

public interface IVehicleService {

    Vehicle saveVehicle(VehicleCreationDTO vehicleCreationDTO);

    Vehicle getVehicleById(Long vehicleId);

    VehiclePageResponseDTO getAllVehicles(int pageNo, int pageSize);
}
