package dev.redy1908.greenway.delivery_path.repository;

import dev.redy1908.greenway.delivery_path.model.DeliveryPath;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryPathRepository extends JpaRepository<DeliveryPath, Long> {
}
