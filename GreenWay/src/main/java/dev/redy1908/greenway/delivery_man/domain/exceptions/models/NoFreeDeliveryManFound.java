package dev.redy1908.greenway.delivery_man.domain.exceptions.models;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class NoFreeDeliveryManFound extends RuntimeException {

    public NoFreeDeliveryManFound() {
        super("No free deliveryMan found.");
    }
}
