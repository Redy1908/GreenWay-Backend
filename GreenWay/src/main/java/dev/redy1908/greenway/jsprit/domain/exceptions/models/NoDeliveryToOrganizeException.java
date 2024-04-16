package dev.redy1908.greenway.jsprit.domain.exceptions.models;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.TOO_EARLY)
public class NoDeliveryToOrganizeException extends RuntimeException{
    public NoDeliveryToOrganizeException() {
        super("All done no Delivery to organize left");
    }
}
