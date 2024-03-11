package dev.redy1908.greenway.delivery.service;

import dev.redy1908.greenway.delivery.dto.DeliveryCreationDto;
import dev.redy1908.greenway.delivery.dto.DeliveryDto;
import dev.redy1908.greenway.delivery.dto.DeliveryPageResponseDTO;

public interface IDeliveryService {

    DeliveryDto createDelivery(DeliveryCreationDto deliveryCreationDto);

    DeliveryDto getDeliveryById(Long deliveryId);

    DeliveryPageResponseDTO getDeliveriesByDeliveryMan(String deliveryManUsername, int pageNo, int pageSize);

    boolean isDeliveryOwner(Long deliveryId, String deliveryManUsername);

}
