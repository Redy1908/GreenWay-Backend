package dev.redy1908.greenway.delivery_vehicle.domain.exceptions.models;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoDeliveryAssignedException extends RuntimeException {

    public NoDeliveryAssignedException(int id) {
        super(String.format("No delivery is assigned to the Vehicle with id: '%d' check after the next scheduling", id));
    }
}
