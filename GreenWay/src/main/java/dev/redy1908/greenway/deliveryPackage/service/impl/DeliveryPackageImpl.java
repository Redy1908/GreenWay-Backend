package dev.redy1908.greenway.deliveryPackage.service.impl;

import dev.redy1908.greenway.deliveryPackage.model.DeliveryPackage;
import dev.redy1908.greenway.deliveryPackage.repository.DeliveryPackageRepository;
import dev.redy1908.greenway.deliveryPackage.service.IDeliveryPackageService;
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
