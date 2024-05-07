package dev.redy1908.greenway.osrm.domain.exceptions.models;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.FAILED_DEPENDENCY)
public class OpentopodataDatasetNotConfiguredException extends RuntimeException {

    public OpentopodataDatasetNotConfiguredException() {
        super("Opentopodata dataset is not configured.");
    }
}
