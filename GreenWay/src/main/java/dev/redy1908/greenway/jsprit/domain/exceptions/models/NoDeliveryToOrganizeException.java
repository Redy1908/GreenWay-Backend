package dev.redy1908.greenway.jsprit.domain.exceptions.models;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoDeliveryToOrganizeException extends RuntimeException{
    public NoDeliveryToOrganizeException() {
        super("No Delivery to organize found");
    }
}
