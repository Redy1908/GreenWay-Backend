package dev.redy1908.greenway.osrm.domain.exceptions.models;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FAILED_DEPENDENCY)
public class CantConnectToOsrmException extends RuntimeException{

    public CantConnectToOsrmException() {
        super("Connection to the OSRM instance refused. Is OSRM running?");
    }
}
