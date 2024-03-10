package dev.redy1908.greenway.delivery.service;

import dev.redy1908.greenway.delivery.dto.DeliveryCreationDto;
import dev.redy1908.greenway.delivery.dto.DeliveryDto;
public interface IDeliveryService {

    DeliveryDto createDelivery(DeliveryCreationDto deliveryCreationDto);

}
