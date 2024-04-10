package dev.redy1908.greenway.delivery_man.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface DeliveryManRepository extends JpaRepository<DeliveryMan, Long> {

    Optional<DeliveryMan> findByUsername(String username);

    Optional<DeliveryMan> findFirstByDeliveryIsNull();

    Page<DeliveryMan> findAllByDeliveryIsNull(Pageable pageable);
}
