package dev.redy1908.greenway.delivery_package.domain;

import java.util.List;
import java.util.Set;

import dev.redy1908.greenway.delivery.domain.Delivery;
import dev.redy1908.greenway.delivery_package.domain.dto.DeliveryPackageDTO;

public interface IDeliveryPackageService {

    void saveAll(List<DeliveryPackage> deliveryPackages);

    Set<DeliveryPackage> setPackagesDelivery(Set<DeliveryPackageDTO> packages, Delivery delivery);

    double calculatePackagesWeight(Set<DeliveryPackage> pakages);

}
