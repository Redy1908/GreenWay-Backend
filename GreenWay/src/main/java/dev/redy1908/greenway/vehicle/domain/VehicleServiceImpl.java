package dev.redy1908.greenway.vehicle.domain;

import dev.redy1908.greenway.app.web.models.PageResponseDTO;
import dev.redy1908.greenway.util.services.PagingService;
import dev.redy1908.greenway.vehicle.domain.dto.VehicleDTO;
import dev.redy1908.greenway.vehicle.domain.exceptions.models.VehicleNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class VehicleServiceImpl extends PagingService<Vehicle, VehicleDTO> implements IVehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;

    @Override
    public Vehicle saveVehicle(VehicleDTO vehicleDto) {
        return vehicleRepository.save(vehicleMapper.toEntity(vehicleDto));
    }

    @Override
    public VehicleDTO findVehicleDTOById(Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElseThrow(
                () -> new VehicleNotFoundException(vehicleId));

        return vehicleMapper.toDto(vehicle);
    }

    @Override
    public Vehicle findVehicleById(Long vehicleId) {
        return vehicleRepository.findById(vehicleId).orElseThrow(
                () -> new VehicleNotFoundException(vehicleId));
    }

    @Override
    public PageResponseDTO<VehicleDTO> getAllVehicles(int pageNo, int pageSize) {

        return createPageResponse(
                () -> vehicleRepository.findAll(PageRequest.of(pageNo, pageSize)));
    }

    @Override
    protected VehicleDTO mapToDto(Vehicle entity) {
        return vehicleMapper.toDto(entity);
    }
}
