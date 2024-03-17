package dev.redy1908.greenway.vehicle.service;

import dev.redy1908.greenway.vehicle.dto.VehicleCreationDTO;
import dev.redy1908.greenway.vehicle.dto.VehicleDTO;
import dev.redy1908.greenway.vehicle.model.Vehicle;
import dev.redy1908.greenway.web.model.PageResponseDTO;

public interface IVehicleService {

    Vehicle saveVehicle(VehicleCreationDTO vehicleCreationDTO);

    VehicleDTO getVehicleById(Long vehicleId);

    PageResponseDTO<VehicleDTO> getAllVehicles(int pageNo, int pageSize);
}
