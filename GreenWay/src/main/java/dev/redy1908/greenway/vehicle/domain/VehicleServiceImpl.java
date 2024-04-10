package dev.redy1908.greenway.vehicle.domain;

import dev.redy1908.greenway.app.web.models.PageResponseDTO;
import dev.redy1908.greenway.util.services.PagingService;
import dev.redy1908.greenway.vehicle.domain.dto.VehicleDTO;
import dev.redy1908.greenway.vehicle.domain.exceptions.models.VehicleAlreadyAssignedException;
import dev.redy1908.greenway.vehicle.domain.exceptions.models.VehicleCapacityExceeded;
import dev.redy1908.greenway.vehicle.domain.exceptions.models.VehicleNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class VehicleServiceImpl extends PagingService<VehicleDTO> implements IVehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;

    @Override
    public Vehicle saveVehicle(VehicleDTO vehicleDto) {
        return vehicleRepository.save(vehicleMapper.toEntity(vehicleDto));
    }

    @Override
    public VehicleDTO findVehicleById(Long vehicleId) {
        return vehicleRepository.findDTOById(vehicleId).orElseThrow(
                () -> new VehicleNotFoundException(vehicleId)
        );
    }

    @Override
    public PageResponseDTO<VehicleDTO> getAllVehicles(int pageNo, int pageSize) {

        return createPageResponse(
                () -> vehicleRepository.findAllBy(PageRequest.of(pageNo, pageSize)));
    }

    @Override
    public PageResponseDTO<VehicleDTO> findAllFreeVehicles(int pageNo, int pageSize) {
        return createPageResponse(
                () -> vehicleRepository.findAllFreeVehicles(PageRequest.of(pageNo, pageSize)));
    }

    @Override
    public VehicleDTO getVehicleIfFree(Long vehicleId) {
        return vehicleRepository.getVehicleIfFree(vehicleId).orElseThrow(VehicleAlreadyAssignedException::new);
    }

    @Override
    public void vehicleCapacitySufficientOrThrow(Vehicle vehicle, double deliveryTotalWeight) {

        if (vehicle.getMaxCapacityKg() < deliveryTotalWeight)
            throw new VehicleCapacityExceeded(vehicle.getMaxCapacityKg(), deliveryTotalWeight);
    }
}
