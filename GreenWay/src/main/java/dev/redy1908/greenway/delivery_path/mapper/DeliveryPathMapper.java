package dev.redy1908.greenway.delivery_path.mapper;

import dev.redy1908.greenway.delivery.dto.DeliveryDTO;
import dev.redy1908.greenway.delivery_path.dto.DeliveryPathDTO;
import dev.redy1908.greenway.delivery_path.model.DeliveryPath;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeliveryPathMapper {

    DeliveryPathDTO toDto(DeliveryPath deliveryPath);

    DeliveryDTO toEntity(DeliveryPathDTO deliveryPathDTO);

}
