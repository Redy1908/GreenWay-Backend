package dev.redy1908.greenway.delivery_vehicle.domain.exceptions.models;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class DeliveryVehicleAlreadyAssignedException extends RuntimeException {

    public DeliveryVehicleAlreadyAssignedException() {
        super("The selected Vehicle is already assigned to a delivery");
    }
}
