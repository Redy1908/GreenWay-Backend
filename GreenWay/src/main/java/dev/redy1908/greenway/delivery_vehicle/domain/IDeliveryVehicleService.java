package dev.redy1908.greenway.delivery_vehicle.domain;

import dev.redy1908.greenway.app.web.models.PageResponseDTO;
import dev.redy1908.greenway.delivery_vehicle.domain.dto.DeliveryVehicleCreationDTO;
import dev.redy1908.greenway.delivery_vehicle.domain.dto.DeliveryVehicleDTO;
import dev.redy1908.greenway.delivery_vehicle.domain.dto.DeliveryVehicleNoDeliveriesDTO;
import dev.redy1908.greenway.osrm.domain.NavigationType;

import java.util.List;
import java.util.Map;

public interface IDeliveryVehicleService {

    DeliveryVehicle save(DeliveryVehicleCreationDTO deliveryVehicleCreationDTO);

    DeliveryVehicle save(DeliveryVehicle deliveryVehicle);

    DeliveryVehicle findById(int id);

    List<DeliveryVehicle> findAllEmptyVehicles();

    PageResponseDTO<DeliveryVehicleNoDeliveriesDTO> findAll(int pageNo, int pageSize);

    DeliveryVehicleDTO findByDeliveryManUsername(String deliveryManUsername);

    Map<String, Object> getRouteNavigationData(int id, NavigationType navigationType);

    boolean isAssociatedWithDeliveryMan(int id, String deliveryManUsername);

    void enterVehicle(int id);

    void leaveVehicle(int id);

}
