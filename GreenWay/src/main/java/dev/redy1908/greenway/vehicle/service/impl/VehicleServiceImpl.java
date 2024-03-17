package dev.redy1908.greenway.vehicle.service.impl;

import dev.redy1908.greenway.app.common.service.PagingService;
import dev.redy1908.greenway.vehicle.dto.VehicleCreationDTO;
import dev.redy1908.greenway.vehicle.dto.VehicleDTO;
import dev.redy1908.greenway.vehicle.exceptions.VehicleNotFoundException;
import dev.redy1908.greenway.vehicle.mapper.VehicleMapper;
import dev.redy1908.greenway.vehicle.model.Vehicle;
import dev.redy1908.greenway.vehicle.repository.VehicleRepository;
import dev.redy1908.greenway.vehicle.service.IVehicleService;
import dev.redy1908.greenway.web.model.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl extends PagingService<Vehicle, VehicleDTO> implements IVehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;

    @Override
    public Vehicle saveVehicle(VehicleCreationDTO vehicleDto) {

        Vehicle vehicle = new Vehicle(
                vehicleDto.model(),
                vehicleDto.batteryNominalCapacity(),
                vehicleDto.vehicleConsumption(),
                vehicleDto.currentBatteryCharge(),
                vehicleDto.maxCapacity()
        );

        return vehicleRepository.save(vehicle);
    }

    @Override
    public VehicleDTO getVehicleById(Long vehicleId) {
        Vehicle vehicle =  vehicleRepository.findById(vehicleId).orElseThrow(
                () -> new VehicleNotFoundException(vehicleId)
        );

        return vehicleMapper.toDto(vehicle);
    }

    @Override
    public PageResponseDTO<VehicleDTO> getAllVehicles(int pageNo, int pageSize) {

        return createPageResponse(
                () -> vehicleRepository.findAll(PageRequest.of(pageNo, pageSize))
        );
    }

    @Override
    protected VehicleDTO mapToDto(Vehicle entity) {
        return vehicleMapper.toDto(entity);
    }
}
