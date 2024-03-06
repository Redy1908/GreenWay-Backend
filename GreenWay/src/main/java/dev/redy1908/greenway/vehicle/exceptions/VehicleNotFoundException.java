package dev.redy1908.greenway.vehicle.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class VehicleNotFoundException extends RuntimeException{
    public VehicleNotFoundException(String vehicleIdentifier) {
        super(String.format("Vehicle with identifier: '%s' not found", vehicleIdentifier));
    }
}
