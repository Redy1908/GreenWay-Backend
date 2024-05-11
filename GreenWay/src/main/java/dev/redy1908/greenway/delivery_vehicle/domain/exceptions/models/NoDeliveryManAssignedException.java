package dev.redy1908.greenway.delivery_vehicle.domain.exceptions.models;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class NoDeliveryManAssignedException extends RuntimeException {

    public NoDeliveryManAssignedException(int id) {
        super("The vehicle with id: '" + id + "' has no deliveryMan assigned");
    }
}
