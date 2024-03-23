package dev.redy1908.greenway.delivery_path.domain;

import dev.redy1908.greenway.delivery.domain.dto.DeliveryDTO;
import dev.redy1908.greenway.delivery_path.domain.dto.DeliveryPathDTO;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface DeliveryPathMapper {

    DeliveryPathDTO toDto(DeliveryPath deliveryPath);

    DeliveryDTO toEntity(DeliveryPathDTO deliveryPathDTO);

}
