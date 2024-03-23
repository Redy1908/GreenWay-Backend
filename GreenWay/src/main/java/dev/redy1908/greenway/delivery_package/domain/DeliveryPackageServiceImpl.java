package dev.redy1908.greenway.delivery_package.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import dev.redy1908.greenway.delivery.domain.Delivery;
import dev.redy1908.greenway.delivery_package.domain.dto.DeliveryPackageDTO;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class DeliveryPackageServiceImpl implements IDeliveryPackageService {

    private final DeliveryPackageRepository deliveryPackageRepository;
    private final DeliveryPackageMapper deliveryPackageMapper;

    @Override
    public void saveAll(List<DeliveryPackage> deliveryPackages) {
        deliveryPackageRepository.saveAll(deliveryPackages);
    }

    @Override
    public Set<DeliveryPackage> setPackagesDelivery(Set<DeliveryPackageDTO> packages, Delivery delivery) {
        Set<DeliveryPackage> deliveryPackages = packages.stream()
                .map(deliveryPackageMapper::toEntity)
                .collect(Collectors.toSet());

        deliveryPackages.forEach(deliveryPackage -> deliveryPackage.setDelivery(delivery));
        deliveryPackageRepository.saveAll(deliveryPackages);

        return deliveryPackages;
    }

    @Override
    public double calculatePackagesWeight(Set<DeliveryPackage> pakages) {
        return pakages.stream().mapToDouble(DeliveryPackage::getWeight).sum();
    }
}
