package dev.redy1908.greenway.delivery_vehicle.domain;

import dev.redy1908.greenway.delivery_vehicle.domain.dto.DeliveryVehicleCreationDTO;
import dev.redy1908.greenway.delivery_vehicle.domain.dto.DeliveryVehicleDTO;
import dev.redy1908.greenway.delivery_vehicle.domain.exceptions.models.VehicleNotFoundException;
import dev.redy1908.greenway.util.services.PagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryVehicleServiceImpl extends PagingService<DeliveryVehicle, DeliveryVehicleDTO> implements IDeliveryVehicleService {

    private final DeliveryVehicleRepository deliveryVehicleRepository;
    private final DeliveryVehicleMapper deliveryVehicleMapper;

    @Override
    public DeliveryVehicle save(DeliveryVehicleCreationDTO deliveryVehicleCreationDTO) {
        DeliveryVehicle deliveryVehicle = deliveryVehicleMapper.vehicleCreationDTOtoVehicle(deliveryVehicleCreationDTO);
        return deliveryVehicleRepository.save(deliveryVehicle);
    }

    @Override
    public DeliveryVehicle save(DeliveryVehicle deliveryVehicle) {
        return deliveryVehicleRepository.save(deliveryVehicle);
    }

    @Override
    public DeliveryVehicle findById(int id) {
        return deliveryVehicleRepository.findById(id).orElseThrow(() -> new VehicleNotFoundException(id));
    }

    @Override
    public List<DeliveryVehicle> findAll() {
        return deliveryVehicleRepository.findAll();
    }

    @Override
    protected DeliveryVehicleDTO mapToDto(DeliveryVehicle entity) {
        return deliveryVehicleMapper.toDto(entity);
    }
}
