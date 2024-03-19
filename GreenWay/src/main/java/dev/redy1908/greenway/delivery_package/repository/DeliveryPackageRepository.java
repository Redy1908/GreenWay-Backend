package dev.redy1908.greenway.delivery_package.repository;

import dev.redy1908.greenway.delivery_package.model.DeliveryPackage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryPackageRepository extends JpaRepository<DeliveryPackage, Long> {
}
