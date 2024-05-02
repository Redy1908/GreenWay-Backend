package dev.redy1908.greenway.osrm.domain.exceptions.models;


import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.FAILED_DEPENDENCY)
public class CantConnectToOpentopoDataException extends RuntimeException{

    public CantConnectToOpentopoDataException() {
        super("Connection to the Opentopodata instance refused. Is Opentopodata running? Is Opendata configured with a dataset?");
    }
}