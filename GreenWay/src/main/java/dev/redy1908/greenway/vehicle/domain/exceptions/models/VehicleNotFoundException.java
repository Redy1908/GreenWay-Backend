package dev.redy1908.greenway.vehicle.domain.exceptions.models;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class VehicleNotFoundException extends RuntimeException {
    public VehicleNotFoundException(Long vehicleId) {
        super(String.format("Vehicle with id: '%s' not found", vehicleId));
    }
}
