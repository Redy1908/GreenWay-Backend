package dev.redy1908.greenway.delivery_man.repository;

import dev.redy1908.greenway.delivery_man.model.DeliveryMan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryManRepository extends JpaRepository<DeliveryMan, Long> {

    Optional<DeliveryMan> findByUsername(String username);
}
