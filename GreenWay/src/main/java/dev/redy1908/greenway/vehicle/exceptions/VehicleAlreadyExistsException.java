package dev.redy1908.greenway.vehicle.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class VehicleAlreadyExistsException extends RuntimeException{
    public VehicleAlreadyExistsException(String vehicleModel) {
        super(String.format("Vehicle with model: '%s' already exists", vehicleModel));
    }
}
