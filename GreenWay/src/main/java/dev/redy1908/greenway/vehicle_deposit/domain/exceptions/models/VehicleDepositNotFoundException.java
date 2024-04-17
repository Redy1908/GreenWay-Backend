package dev.redy1908.greenway.vehicle_deposit.domain.exceptions.models;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class VehicleDepositNotFoundException extends RuntimeException {

    public VehicleDepositNotFoundException() {
        super("The deposit location is not available, please set the deposit location");
    }
}
