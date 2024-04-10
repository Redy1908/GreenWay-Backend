package dev.redy1908.greenway.delivery_man.domain;

import dev.redy1908.greenway.delivery_man.domain.dto.DeliveryManDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface DeliveryManMapper {

    DeliveryMan toEntity(DeliveryManDTO deliveryManDTO);

    DeliveryManDTO toDTO(DeliveryMan deliveryMan);

}
