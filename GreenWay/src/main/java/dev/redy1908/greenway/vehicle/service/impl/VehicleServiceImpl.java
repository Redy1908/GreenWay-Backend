package dev.redy1908.greenway.vehicle.service.impl;

import dev.redy1908.greenway.vehicle.dto.VehicleDto;
import dev.redy1908.greenway.vehicle.dto.VehiclePageResponseDTO;
import dev.redy1908.greenway.vehicle.exceptions.VehicleNotFoundException;
import dev.redy1908.greenway.vehicle.mapper.VehicleMapper;
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
    private final VehicleMapper vehicleMapper;

    @Override
    public void saveVehicle(VehicleDto vehicleDto) {
        Vehicle vehicle = vehicleMapper.toEntity(vehicleDto);
        vehicle.setIsFree(true);

        vehicleRepository.save(vehicle);
    }

    @Override
    public VehicleDto getVehicleById(Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElseThrow(
                () -> new VehicleNotFoundException(vehicleId)
        );

        return vehicleMapper.toDto(vehicle);
    }

    @Override
    public VehiclePageResponseDTO getFreeVehicles(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Vehicle> vehicles = vehicleRepository.findAllByIsFreeTrue(pageable);
        List<Vehicle> listVehicles = vehicles.getContent();
        List<VehicleDto> content =  listVehicles.stream().map(vehicleMapper::toDto).toList();

        VehiclePageResponseDTO vehiclePageResponseDTO = new VehiclePageResponseDTO();
        vehiclePageResponseDTO.setContent(content);
        vehiclePageResponseDTO.setPageNo(vehicles.getNumber());
        vehiclePageResponseDTO.setPageSize(vehicles.getSize());
        vehiclePageResponseDTO.setTotalElements(vehicles.getTotalElements());
        vehiclePageResponseDTO.setTotalPages(vehicles.getTotalPages());
        vehiclePageResponseDTO.setLast(vehicles.isLast());

        return vehiclePageResponseDTO;
    }

    @Override
    public VehiclePageResponseDTO getAllVehicles(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Vehicle> vehicles = vehicleRepository.findAll(pageable);
        List<Vehicle> listVehicles = vehicles.getContent();
        List<VehicleDto> content =  listVehicles.stream().map(vehicleMapper::toDto).toList();

        VehiclePageResponseDTO vehiclePageResponseDTO = new VehiclePageResponseDTO();
        vehiclePageResponseDTO.setContent(content);
        vehiclePageResponseDTO.setPageNo(vehicles.getNumber());
        vehiclePageResponseDTO.setPageSize(vehicles.getSize());
        vehiclePageResponseDTO.setTotalElements(vehicles.getTotalElements());
        vehiclePageResponseDTO.setTotalPages(vehicles.getTotalPages());
        vehiclePageResponseDTO.setLast(vehicles.isLast());

        return vehiclePageResponseDTO;
    }
}
