package dev.redy1908.greenway.osrm.domain.exceptions.models;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InvalidOsrmResponseException extends RuntimeException{

    public InvalidOsrmResponseException() {
        super("Got an invalid OSRM response");
    }
}
