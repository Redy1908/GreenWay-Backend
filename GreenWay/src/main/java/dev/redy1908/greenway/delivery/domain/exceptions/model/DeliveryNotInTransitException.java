package dev.redy1908.greenway.delivery.domain.exceptions.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DeliveryNotInTransitException extends RuntimeException{

    public DeliveryNotInTransitException(int id){
        super("Can't complete the delivery with id:'" + id + "'. The delivery is not in transit.");
    }
}
