package dev.redy1908.greenway.delivery_package.domain;

import java.util.Set;

public interface IDeliveryPackageService {

    double calculatePackagesWeight(Set<DeliveryPackage> packages);

}
