package dev.redy1908.greenway.delivery_vehicle.domain.exceptions.models;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DeliveryVehicleNotFoundException extends RuntimeException {
    public DeliveryVehicleNotFoundException(int vehicleId) {
        super(String.format("Vehicle with id: '%d' not found", vehicleId));
    }

    public DeliveryVehicleNotFoundException(String deliveryManUsername) {
        super(String.format("Vehicle with associated deliveryman: '%s' not found", deliveryManUsername));
    }
}
