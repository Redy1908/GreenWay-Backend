package dev.redy1908.greenway.delivery_package.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
class DeliveryPackageServiceImpl implements IDeliveryPackageService {

    private final DeliveryPackageRepository deliveryPackageRepository;

    @Override
    public void saveAll(Set<DeliveryPackage> deliveryPackages) {
        deliveryPackageRepository.saveAll(deliveryPackages);
    }

    @Override
    public double calculatePackagesWeight(Set<DeliveryPackage> packages) {
        return packages.stream().mapToDouble(DeliveryPackage::getWeight).sum();
    }
}
