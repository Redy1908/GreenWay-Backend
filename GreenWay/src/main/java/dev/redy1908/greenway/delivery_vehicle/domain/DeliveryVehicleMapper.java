package dev.redy1908.greenway.delivery_vehicle.domain;

import dev.redy1908.greenway.delivery_vehicle.domain.dto.DeliveryVehicleCreationDTO;
import dev.redy1908.greenway.delivery_vehicle.domain.dto.DeliveryVehicleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeliveryVehicleMapper {

    DeliveryVehicleDTO deliveryVehicleToDeliveryVehicleDTO(DeliveryVehicle deliveryVehicle);

    DeliveryVehicle vehicleCreationDTOtoVehicle(DeliveryVehicleCreationDTO deliveryVehicleCreationDTO);
}
