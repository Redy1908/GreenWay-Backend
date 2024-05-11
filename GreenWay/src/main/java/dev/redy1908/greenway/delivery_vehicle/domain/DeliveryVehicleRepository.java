package dev.redy1908.greenway.delivery_vehicle.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeliveryVehicleRepository extends JpaRepository<DeliveryVehicle, Integer> {

    List<DeliveryVehicle> findAllByTripIsNull();

    Optional<DeliveryVehicle> findByDeliveryMan_Username(String deliveryManUsername);

    boolean existsByIdAndDeliveryMan_Username(int vehicleId, String deliveryManUsername);
}
