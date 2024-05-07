package dev.redy1908.greenway.osrm.domain.exceptions.models;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OpentopodataTooManyLocationsException extends RuntimeException{

    public OpentopodataTooManyLocationsException() {
        super("Max number of locations (points) limit is 1000");
    }
}
