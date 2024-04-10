package dev.redy1908.greenway.delivery_man.domain.exceptions.models;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.ALREADY_REPORTED)
public class DeliveryManAlreadyExistsException extends RuntimeException{

    public DeliveryManAlreadyExistsException(){
        super("Deliveryman already exists, no further action is required.");
    }
}
