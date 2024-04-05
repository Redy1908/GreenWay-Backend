package dev.redy1908.greenway.delivery.domain;

import dev.redy1908.greenway.delivery.domain.dto.DeliveryDTO;
import dev.redy1908.greenway.delivery_package.domain.DeliveryPackageMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(uses = DeliveryPackageMapper.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface DeliveryMapper {

    @Mapping(source = "id", target = "deliveryId")
    @Mapping(source = "vehicle.id", target = "vehicleId")
    @Mapping(source = "deliveryMan.username", target = "deliveryManUsername")
    DeliveryDTO toDto(Delivery delivery);
}
