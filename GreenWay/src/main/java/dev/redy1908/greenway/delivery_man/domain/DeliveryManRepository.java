package dev.redy1908.greenway.delivery_man.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

interface DeliveryManRepository extends JpaRepository<DeliveryMan, Long> {

    Optional<DeliveryMan> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query("SELECT dm FROM DeliveryMan dm LEFT JOIN Delivery del ON dm.id = del.deliveryMan.id WHERE del IS NULL")
    Optional<DeliveryMan> findFirstFreeDeliveryMan();

    @Query("SELECT dm FROM DeliveryMan dm LEFT JOIN Delivery del ON dm.id = del.deliveryMan.id WHERE del IS NULL")
    Page<DeliveryMan> findAllFreeDeliveryMan(Pageable pageable);
}
