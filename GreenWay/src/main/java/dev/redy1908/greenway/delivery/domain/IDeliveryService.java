package dev.redy1908.greenway.delivery.domain;

import dev.redy1908.greenway.app.web.models.PageResponseDTO;
import dev.redy1908.greenway.delivery.domain.dto.DeliveryCreationDTO;
import dev.redy1908.greenway.delivery.domain.dto.DeliveryDTO;

import java.util.List;

public interface IDeliveryService {

    Delivery save(DeliveryCreationDTO deliveryCreationDTO);

    Delivery findById(int id);

    PageResponseDTO<DeliveryDTO> findAll(int pageNo, int pageSize);

    List<Delivery> findUnassignedDeliveries();

    void completeDelivery(int id);
}
