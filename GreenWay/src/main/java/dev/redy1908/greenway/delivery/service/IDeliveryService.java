package dev.redy1908.greenway.delivery.service;

import dev.redy1908.greenway.delivery.dto.DeliveryCreationDto;
import dev.redy1908.greenway.delivery.dto.DeliveryDTO;
import dev.redy1908.greenway.delivery.model.Delivery;
import dev.redy1908.greenway.web.model.PageResponseDTO;

public interface IDeliveryService {

    Delivery createDelivery(DeliveryCreationDto deliveryCreationDto);

    DeliveryDTO getDeliveryById(Long deliveryId);

    PageResponseDTO<DeliveryDTO> getAllDeliveries(int pageNo, int pageSize);

    PageResponseDTO<DeliveryDTO> getDeliveriesByDeliveryMan(String deliveryManUsername, int pageNo, int pageSize);

    boolean isDeliveryOwner(Long deliveryId, String deliveryManUsername);

    void selectDelivery(Long deliveryID, String deliveryManUsername);
}
