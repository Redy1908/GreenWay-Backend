package dev.redy1908.greenway.vehicle_deposit.domain.exceptions.models;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.ALREADY_REPORTED)
public class VehicleDepositAlreadyExistsException extends RuntimeException{
    public VehicleDepositAlreadyExistsException() {
        super("Deposit already exists, you can update it");
    }
}
