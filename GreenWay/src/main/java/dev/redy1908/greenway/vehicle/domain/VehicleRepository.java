package dev.redy1908.greenway.vehicle.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Page<Vehicle> findAllByDeliveryIsNull(Pageable pageable);
}
