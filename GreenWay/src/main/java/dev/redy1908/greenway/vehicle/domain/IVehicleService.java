package dev.redy1908.greenway.vehicle.domain;

import dev.redy1908.greenway.app.web.models.PageResponseDTO;
import dev.redy1908.greenway.vehicle.domain.dto.VehicleDTO;

public interface IVehicleService {

    Vehicle saveVehicle(VehicleDTO vehicleDTO);

    VehicleDTO findVehicleDTOById(Long vehicleId);

    Vehicle findVehicleById(Long vehicleId);

    PageResponseDTO<VehicleDTO> getAllVehicles(int pageNo, int pageSize);

    PageResponseDTO<VehicleDTO> findAllFreeVehicles(int pageNo, int pageSize);
}
