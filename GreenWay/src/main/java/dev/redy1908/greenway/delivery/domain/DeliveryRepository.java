package dev.redy1908.greenway.delivery.domain;

import dev.redy1908.greenway.delivery.domain.dto.DeliveryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    Page<DeliveryDTO> findAllBy(Pageable pageable);

    Optional<DeliveryDTO> findDeliveryDTOByIdAndDeliveryMan_Username(Long deliveryId, String deliveryManUsername);
    Page<DeliveryDTO> findAllByDeliveryMan_Username(String deliveryManUsername, Pageable pageable);
}
