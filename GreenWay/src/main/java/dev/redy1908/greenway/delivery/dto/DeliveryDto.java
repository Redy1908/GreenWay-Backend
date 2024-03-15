package dev.redy1908.greenway.delivery.dto;

import java.util.ArrayList;

public record DeliveryDto(

        Long id,
        ArrayList<Double> startPoint,
        ArrayList<Double> endPoint,
        String encodedPolyline

) {
}