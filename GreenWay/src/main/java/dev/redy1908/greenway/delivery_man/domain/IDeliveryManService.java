package dev.redy1908.greenway.delivery_man.domain;

import dev.redy1908.greenway.app.web.models.PageResponseDTO;
import dev.redy1908.greenway.delivery_man.domain.dto.DeliveryManDTO;

public interface IDeliveryManService {

    void save(String username);

    DeliveryMan findByUsername(String username);

    DeliveryMan findFirstByDeliveryIsNull();

    PageResponseDTO<DeliveryManDTO> findAllFreeDeliveryMan(int pageNo, int pageSize);

}
