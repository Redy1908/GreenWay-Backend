package dev.redy1908.greenway.delivery_package.service.impl;

import dev.redy1908.greenway.delivery_package.model.DeliveryPackage;
import dev.redy1908.greenway.delivery_package.repository.DeliveryPackageRepository;
import dev.redy1908.greenway.delivery_package.service.IDeliveryPackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryPackageImpl implements IDeliveryPackageService {

    private final DeliveryPackageRepository deliveryPackageRepository;

    @Override
    public void saveAll(List<DeliveryPackage> deliveryPackages) {
        deliveryPackageRepository.saveAll(deliveryPackages);
    }
}
