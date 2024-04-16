package dev.redy1908.greenway.delivery_vehicle.domain.exceptions.models;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class DeliveryVehicleCapacityExceeded extends RuntimeException {

    public DeliveryVehicleCapacityExceeded(Double vehicleCapacity, Double deliveryTotal) {
        super(String.format("Vehicle Capacity %.2f kg Exceeded, delivery total: %.2f", vehicleCapacity, deliveryTotal));
    }
}
