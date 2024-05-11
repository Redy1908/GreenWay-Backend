package dev.redy1908.greenway.delivery.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Integer> {

    List<Delivery> findAllByScheduledIsFalseAndDeliveryTimeIsNull();
}
