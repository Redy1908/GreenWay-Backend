package dev.redy1908.greenway.delivery.service;

import dev.redy1908.greenway.delivery.dto.DeliveryCreationDto;
import dev.redy1908.greenway.delivery.dto.DeliveryPageResponseDTO;
import dev.redy1908.greenway.delivery.model.Delivery;

public interface IDeliveryService {

    Delivery createDelivery(DeliveryCreationDto deliveryCreationDto);

    Delivery getDeliveryById(Long deliveryId);

    DeliveryPageResponseDTO getAllDeliveries(int pageNo, int pageSize);

    DeliveryPageResponseDTO getDeliveriesByDeliveryMan(String deliveryManUsername, int pageNo, int pageSize);

    boolean isDeliveryOwner(Long deliveryId, String deliveryManUsername);

    void selectDelivery(Long deliveryID, String deliveryManUsername);
}
