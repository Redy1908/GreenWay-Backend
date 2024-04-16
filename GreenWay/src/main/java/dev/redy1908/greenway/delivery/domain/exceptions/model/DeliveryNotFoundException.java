package dev.redy1908.greenway.delivery.domain.exceptions.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DeliveryNotFoundException extends RuntimeException {

    public DeliveryNotFoundException(int id) {
        super(String.format("Delivery with : '%d' not found.", id));
    }
}
