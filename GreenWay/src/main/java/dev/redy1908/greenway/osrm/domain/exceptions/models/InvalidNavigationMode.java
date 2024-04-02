package dev.redy1908.greenway.osrm.domain.exceptions.models;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class InvalidNavigationMode extends RuntimeException{
    public InvalidNavigationMode() {
        super("Invalid navigation mode");
    }
}
