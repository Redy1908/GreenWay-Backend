package dev.redy1908.greenway.delivery_path.mapper;

import dev.redy1908.greenway.delivery.dto.DeliveryDTO;
import dev.redy1908.greenway.delivery_path.dto.DeliveryPathDTO;
import dev.redy1908.greenway.delivery_path.model.DeliveryPath;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface DeliveryPathMapper {

    DeliveryPathDTO toDto(DeliveryPath deliveryPath);

    DeliveryDTO toEntity(DeliveryPathDTO deliveryPathDTO);

}
