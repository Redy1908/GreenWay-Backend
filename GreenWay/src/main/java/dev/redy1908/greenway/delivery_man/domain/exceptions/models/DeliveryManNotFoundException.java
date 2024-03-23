package dev.redy1908.greenway.delivery_man.domain.exceptions.models;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DeliveryManNotFoundException extends RuntimeException {
    public DeliveryManNotFoundException(String username) {
        super(String.format("DeliveryMan with username: '%s' not found", username));
    }
}
