package dev.redy1908.greenway.deliveryPath.mapper;

import dev.redy1908.greenway.delivery.dto.DeliveryDTO;
import dev.redy1908.greenway.deliveryPath.dto.DeliveryPathDTO;
import dev.redy1908.greenway.deliveryPath.model.DeliveryPath;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface DeliveryPathMapper {

    DeliveryPathDTO toDto(DeliveryPath deliveryPath);

    DeliveryDTO toEntity(DeliveryPathDTO deliveryPathDTO);

}
