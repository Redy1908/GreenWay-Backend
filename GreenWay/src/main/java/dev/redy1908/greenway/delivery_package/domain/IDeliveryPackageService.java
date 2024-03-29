package dev.redy1908.greenway.delivery_package.domain;

import java.util.Set;

public interface IDeliveryPackageService {

    void saveAll(Set<DeliveryPackage> deliveryPackages);

    double calculatePackagesWeight(Set<DeliveryPackage> packages);

}
