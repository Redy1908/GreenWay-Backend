package dev.redy1908.greenway.delivery.dto;

import dev.redy1908.greenway.chargingStations.model.ChargingStation;

import java.util.ArrayList;
import java.util.Set;

public record DeliveryDto(

        Long id,

        ArrayList<Double> startPoint,
        ArrayList<Double> endPoint,

        String encodedPolyline,

        Set<ChargingStation> chargingStations

) {
}