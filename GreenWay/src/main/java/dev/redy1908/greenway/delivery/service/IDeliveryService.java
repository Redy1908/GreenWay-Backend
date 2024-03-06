package dev.redy1908.greenway.delivery.service;

import dev.redy1908.greenway.delivery.dto.DeliveryCreationDto;
import dev.redy1908.greenway.delivery.model.Delivery;

public interface IDeliveryService {

    Delivery createDelivery(DeliveryCreationDto deliveryCreationDto);

}
