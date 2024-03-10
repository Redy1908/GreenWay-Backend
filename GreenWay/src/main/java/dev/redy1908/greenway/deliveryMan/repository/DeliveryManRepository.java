package dev.redy1908.greenway.deliveryMan.repository;

import dev.redy1908.greenway.deliveryMan.model.DeliveryMan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryManRepository extends JpaRepository<DeliveryMan, Long> {

    Optional<DeliveryMan> findByUsername(String username);
}
