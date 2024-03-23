package dev.redy1908.greenway.vehicle.domain.exceptions.models;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class VehicleCapacityExceeded extends RuntimeException {

    public VehicleCapacityExceeded(Double vehicleCapacity, Double deliveryTotal) {
        super(String.format("Vehicle Capacity %.2f kg Exceeded, delivery total: %.2f", vehicleCapacity, deliveryTotal));
    }
}
