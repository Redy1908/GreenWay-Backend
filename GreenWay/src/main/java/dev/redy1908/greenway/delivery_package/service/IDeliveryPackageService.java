package dev.redy1908.greenway.delivery_package.service;

import dev.redy1908.greenway.delivery_package.model.DeliveryPackage;

import java.util.List;

public interface IDeliveryPackageService {

    void saveAll(List<DeliveryPackage> deliveryPackages);
}
