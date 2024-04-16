package dev.redy1908.greenway.delivery.domain;

import dev.redy1908.greenway.delivery.domain.dto.DeliveryCreationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeliveryMapper {

    Delivery deliveryCreationDTOtoDelivery(DeliveryCreationDTO deliveryCreationDTO);
}
