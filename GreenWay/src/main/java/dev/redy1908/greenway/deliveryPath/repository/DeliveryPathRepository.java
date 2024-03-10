package dev.redy1908.greenway.deliveryPath.repository;

import dev.redy1908.greenway.deliveryPath.model.DeliveryPath;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryPathRepository extends JpaRepository<DeliveryPath, Long> {
}
