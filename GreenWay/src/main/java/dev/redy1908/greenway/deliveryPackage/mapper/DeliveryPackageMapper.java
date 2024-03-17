package dev.redy1908.greenway.deliveryPackage.mapper;

import dev.redy1908.greenway.deliveryPackage.dto.DeliveryPackageDTO;
import dev.redy1908.greenway.deliveryPackage.model.DeliveryPackage;
import dev.redy1908.greenway.point.mapper.PointMapper;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(uses = PointMapper.class)
public interface DeliveryPackageMapper {

    DeliveryPackageDTO toDto(DeliveryPackage deliveryPackage);

    DeliveryPackage toEntity(DeliveryPackageDTO deliveryPackageDTO);
}
