package dev.redy1908.greenway.jsprit.domain.exceptions.models;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoDeliveryManToOrganizeException extends RuntimeException {

    public NoDeliveryManToOrganizeException() {
        super("No free delivery man to organize found.");
    }
}
