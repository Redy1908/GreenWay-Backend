package dev.redy1908.greenway.deliveryPackage.repository;

import dev.redy1908.greenway.deliveryPackage.model.DeliveryPackage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryPackageRepository extends JpaRepository<DeliveryPackage, Long> {
}
