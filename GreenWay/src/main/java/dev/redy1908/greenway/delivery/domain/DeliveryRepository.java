package dev.redy1908.greenway.delivery.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    Page<Delivery> findAllByDeliveryManIsNull(Pageable pageable);

    Optional<Delivery> getDeliveryByIdAndDeliveryMan_Username(Long deliveryId, String deliveryManUsername);

    boolean existsByVehicle_Id(Long vehicleId);
}
