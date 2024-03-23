package dev.redy1908.greenway.vehicle.domain;

import dev.redy1908.greenway.app.web.models.PageResponseDTO;
import dev.redy1908.greenway.vehicle.domain.dto.VehicleCreationDTO;
import dev.redy1908.greenway.vehicle.domain.dto.VehicleDTO;

public interface IVehicleService {

    Vehicle saveVehicle(VehicleCreationDTO vehicleCreationDTO);

    VehicleDTO getVehicleById(Long vehicleId);

    PageResponseDTO<VehicleDTO> getAllVehicles(int pageNo, int pageSize);
}