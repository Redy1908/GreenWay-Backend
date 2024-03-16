package dev.redy1908.greenway.deliveryPackage.service;

import dev.redy1908.greenway.deliveryPackage.model.DeliveryPackage;

import java.util.List;

public interface IDeliveryPackageService {

    List<DeliveryPackage> saveAll(List<DeliveryPackage> deliveryPackages);
}
