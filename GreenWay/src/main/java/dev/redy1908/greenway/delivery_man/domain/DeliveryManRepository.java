package dev.redy1908.greenway.delivery_man.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeliveryManRepository extends JpaRepository<DeliveryMan, Long> {

    Optional<DeliveryMan> findByUsername(String username);

    List<DeliveryMan> findAllByDeliveryVehicleIsNull();
}
