package dev.redy1908.greenway.delivery.domain;

import dev.redy1908.greenway.app.web.models.PageResponseDTO;
import dev.redy1908.greenway.delivery.domain.dto.DeliveryDTO;
import dev.redy1908.greenway.delivery.domain.dto.DeliveryWithNavigationDTO;
import jakarta.validation.Valid;

public interface IDeliveryService {

    Delivery createDelivery(@Valid DeliveryDTO deliveryCreationDTO);

    DeliveryWithNavigationDTO getDeliveryByIdWithNavigation(Long deliveryId, String navigationType);

    PageResponseDTO<DeliveryDTO> getAllDeliveries(int pageNo, int pageSize);

    PageResponseDTO<DeliveryDTO> getAllDeliveriesByDeliveryMan(String deliveryManUsername, int pageNo, int pageSize);

    boolean isDeliveryOwner(Long deliveryId, String deliveryManUsername);
}
