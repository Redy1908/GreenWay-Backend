package dev.redy1908.greenway.vehicle.domain.exceptions.models;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class VehicleAutonomyNotSufficientException extends RuntimeException{

    public VehicleAutonomyNotSufficientException(double vehicleAutonomy, List<Double> tripDistances) {
        super(String.format("Vehicle autonomy (%.2f km) is not sufficient for the selected Trip: " +
                        "- Distance mode: %.2f km - Duration mode: %.2f km - Elevation mode: %.2f km - Standard mode: %.2f km " +
                        "Create a shorter Trip or select another Vehicle",
                vehicleAutonomy, tripDistances.getFirst(), tripDistances.get(1), tripDistances.get(2), tripDistances.get(3)));
    }


    public VehicleAutonomyNotSufficientException(double vehicleAutonomy, double tripDistance) {
        super(String.format("Vehicle autonomy (%.2f km) is not sufficient for the selected Trip: (%.2f km) use another navigation mode",
                vehicleAutonomy, tripDistance));
    }
}
