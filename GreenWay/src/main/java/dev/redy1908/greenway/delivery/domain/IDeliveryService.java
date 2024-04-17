package dev.redy1908.greenway.delivery.domain;

import dev.redy1908.greenway.app.web.models.PageResponseDTO;
import dev.redy1908.greenway.delivery.domain.dto.DeliveryDTO;

import java.util.List;

public interface IDeliveryService {

    Delivery save(Delivery delivery);

    Delivery findById(int id);

    PageResponseDTO<DeliveryDTO> findAll(int pageNo, int pageSize);

    List<Delivery> findAllByDeliveryVehicleNull();

}
