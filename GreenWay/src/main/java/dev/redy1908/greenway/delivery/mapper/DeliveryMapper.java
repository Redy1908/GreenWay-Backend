package dev.redy1908.greenway.delivery.mapper;

import dev.redy1908.greenway.delivery.dto.DeliveryDto;
import dev.redy1908.greenway.delivery.model.Delivery;
import dev.redy1908.greenway.deliveryPath.model.DeliveryPath;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;

@Component
public class DeliveryMapper {

    public DeliveryDto toDto(Delivery delivery){
        if(delivery == null) return null;

        DeliveryPath deliveryPath = delivery.getDeliveryPath();

        ArrayList<Double> startPoint = new ArrayList<>(
                Arrays.asList(
                        deliveryPath.getStartPoint().getX(),
                        deliveryPath.getStartPoint().getY()
                )
        );

        ArrayList<Double> endPoint = new ArrayList<>(
                Arrays.asList(
                        deliveryPath.getEndPoint().getX(),
                        deliveryPath.getEndPoint().getY()
                )
        );

        return new DeliveryDto(
                delivery.getId(),
                startPoint,
                endPoint,
                deliveryPath.getEncodedPolyline()
        );

    }
}
