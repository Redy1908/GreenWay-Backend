package dev.redy1908.greenway.vehicle.service.impl;

import dev.redy1908.greenway.vehicle.dto.VehicleDto;
import dev.redy1908.greenway.vehicle.exceptions.VehicleAlreadyExistsException;
import dev.redy1908.greenway.vehicle.exceptions.VehicleNotFoundException;
import dev.redy1908.greenway.vehicle.mapper.VehicleMapper;
import dev.redy1908.greenway.vehicle.model.Vehicle;
import dev.redy1908.greenway.vehicle.repository.VehicleRepository;
import dev.redy1908.greenway.vehicle.service.IVehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements IVehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;

    @Override
    public void saveVehicle(VehicleDto vehicleDto) {
        Vehicle vehicle = vehicleMapper.toEntity(vehicleDto);
        vehicle.setIsFree(true);

        if(vehicleRepository.findByModel(vehicle.getModel()).isPresent()){
            throw new VehicleAlreadyExistsException(vehicle.getModel());
        }

        vehicleRepository.save(vehicle);
    }

    @Override
    public VehicleDto getVehicle(String vehicleModel) {
        Vehicle vehicle = vehicleRepository.findByModel(vehicleModel).orElseThrow(
                () -> new VehicleNotFoundException(vehicleModel)
        );

        return vehicleMapper.toDto(vehicle);
    }

    @Override
    public void deleteVehicle(String vehicleModel) {
        Vehicle vehicle = vehicleRepository.findByModel(vehicleModel).orElseThrow(
                () -> new VehicleNotFoundException(vehicleModel)
        );

        vehicleRepository.delete(vehicle);
    }
}
