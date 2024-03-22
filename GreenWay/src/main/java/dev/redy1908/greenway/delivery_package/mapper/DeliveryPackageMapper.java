package dev.redy1908.greenway.delivery_package.mapper;

import dev.redy1908.greenway.delivery_package.dto.DeliveryPackageDTO;
import dev.redy1908.greenway.delivery_package.model.DeliveryPackage;
import dev.redy1908.greenway.point.mapper.PointMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(uses = PointMapper.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeliveryPackageMapper {

    DeliveryPackageDTO toDto(DeliveryPackage deliveryPackage);

    DeliveryPackage toEntity(DeliveryPackageDTO deliveryPackageDTO);
}
