package dev.redy1908.greenway.delivery.domain.exceptions.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.ALREADY_REPORTED)
public class DeliveryAlreadyCompletedException extends RuntimeException {

    public DeliveryAlreadyCompletedException(int id) {
        super("Delivery with id '" + id + "' already completed");
    }
}
