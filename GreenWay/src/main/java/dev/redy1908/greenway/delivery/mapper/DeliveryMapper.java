package dev.redy1908.greenway.delivery.mapper;

import dev.redy1908.greenway.delivery.dto.DeliveryDTO;
import dev.redy1908.greenway.delivery.model.Delivery;
import dev.redy1908.greenway.delivery_package.mapper.DeliveryPackageMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(uses = DeliveryPackageMapper.class)
public interface DeliveryMapper {

    @Mapping(source = "vehicle.id", target = "vehicleId")
    DeliveryDTO toDto(Delivery delivery);

    Delivery toEntity(DeliveryDTO deliveryDTO);
}
