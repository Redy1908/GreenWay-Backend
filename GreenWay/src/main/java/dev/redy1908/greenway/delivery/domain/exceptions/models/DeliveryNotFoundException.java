package dev.redy1908.greenway.delivery.domain.exceptions.models;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DeliveryNotFoundException extends RuntimeException {
    public DeliveryNotFoundException(Long deliveryId) {
        super(String.format("Delivery with id: '%d' not found", deliveryId));
    }
}
