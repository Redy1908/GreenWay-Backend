package dev.redy1908.greenway.delivery_vehicle.domain.exceptions.models;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class VehicleNotFoundException extends RuntimeException {
    public VehicleNotFoundException(int vehicleId) {
        super(String.format("Vehicle with id: '%d' not found", vehicleId));
    }
}
