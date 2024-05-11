package dev.redy1908.greenway.osrm.domain.exceptions.models;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class OpentopodataConnectionRefusedException extends RuntimeException {

    public OpentopodataConnectionRefusedException() {
        super("Connection to Opentopodata refused. Is the instance running?");
    }
}
