package dev.redy1908.greenway.delivery_package.domain;

import dev.redy1908.greenway.delivery_package.domain.dto.DeliveryPackageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeliveryPackageMapper {

    DeliveryPackageDTO toDto(DeliveryPackage deliveryPackage);

    DeliveryPackage toEntity(DeliveryPackageDTO deliveryPackageDTO);
}
