package dev.redy1908.greenway.vehicle.service.impl;

import dev.redy1908.greenway.vehicle.dto.VehicleCreationDTO;
import dev.redy1908.greenway.vehicle.dto.VehiclePageResponseDTO;
import dev.redy1908.greenway.vehicle.exceptions.VehicleNotFoundException;
import dev.redy1908.greenway.vehicle.model.Vehicle;
import dev.redy1908.greenway.vehicle.repository.VehicleRepository;
import dev.redy1908.greenway.vehicle.service.IVehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements IVehicleService {

    private final VehicleRepository vehicleRepository;

    @Override
    public Vehicle saveVehicle(VehicleCreationDTO vehicleDto) {

        Vehicle vehicle = new Vehicle(
                vehicleDto.model(),
                vehicleDto.batteryNominalCapacity(),
                vehicleDto.vehicleConsumption(),
                vehicleDto.currentBatteryCharge()
        );

        return vehicleRepository.save(vehicle);
    }

    @Override
    public Vehicle getVehicleById(Long vehicleId) {
        return vehicleRepository.findById(vehicleId).orElseThrow(
                () -> new VehicleNotFoundException(vehicleId)
        );
    }

    @Override
    public VehiclePageResponseDTO getAllVehicles(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Vehicle> vehicles = vehicleRepository.findAll(pageable);
        List<Vehicle> listVehicles = vehicles.getContent();

        VehiclePageResponseDTO vehiclePageResponseDTO = new VehiclePageResponseDTO();
        vehiclePageResponseDTO.setContent(listVehicles);
        vehiclePageResponseDTO.setPageNo(vehicles.getNumber());
        vehiclePageResponseDTO.setPageSize(vehicles.getSize());
        vehiclePageResponseDTO.setTotalElements(vehicles.getTotalElements());
        vehiclePageResponseDTO.setTotalPages(vehicles.getTotalPages());
        vehiclePageResponseDTO.setLast(vehicles.isLast());

        return vehiclePageResponseDTO;
    }
}
