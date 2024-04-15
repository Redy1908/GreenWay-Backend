package dev.redy1908.greenway.delivery.domain;

import dev.redy1908.greenway.delivery.domain.dto.DeliveryCreationDTO;
import dev.redy1908.greenway.delivery.domain.dto.DeliveryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeliveryMapper {

    DeliveryDTO toDto(Delivery delivery);

    Delivery deliveryCreationDTOtoDelivery(DeliveryCreationDTO deliveryCreationDTO);
}
