package dev.redy1908.greenway.delivery.domain;

import dev.redy1908.greenway.app.web.models.PageResponseDTO;
import dev.redy1908.greenway.delivery.domain.dto.DeliveryCreationDto;
import dev.redy1908.greenway.delivery.domain.dto.DeliveryDTO;

public interface IDeliveryService {

    Delivery createDelivery(DeliveryCreationDto deliveryCreationDto);

    DeliveryDTO getDeliveryById(Long deliveryId);

    PageResponseDTO<DeliveryDTO> getAllDeliveries(int pageNo, int pageSize);

    PageResponseDTO<DeliveryDTO> getAllUnassignedDeliveries(int pageNo, int pageSize);

    boolean isDeliveryOwner(Long deliveryId, String deliveryManUsername);

    void selectDelivery(Long deliveryID, String deliveryManUsername);
}
