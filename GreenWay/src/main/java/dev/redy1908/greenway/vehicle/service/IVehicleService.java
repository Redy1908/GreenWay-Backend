package dev.redy1908.greenway.vehicle.service;

import dev.redy1908.greenway.vehicle.dto.VehicleDto;

public interface IVehicleService {

    void saveVehicle(VehicleDto vehicleDto);

    VehicleDto getVehicle(String vehicleModel);

    void updateVehicle(VehicleDto vehicleDto);

    void deleteVehicle(String vehicleModel);
}
