package dev.redy1908.greenway.delivery_vehicle.domain;

import dev.redy1908.greenway.app.web.models.PageResponseDTO;
import dev.redy1908.greenway.delivery_vehicle.domain.dto.DeliveryVehicleCreationDTO;
import dev.redy1908.greenway.delivery_vehicle.domain.dto.DeliveryVehicleDTO;
import dev.redy1908.greenway.delivery_vehicle.domain.dto.DeliveryVehicleNoDeliveriesDTO;

import java.util.List;
import java.util.Map;

public interface IDeliveryVehicleService {

    DeliveryVehicle save(DeliveryVehicleCreationDTO deliveryVehicleCreationDTO);

    DeliveryVehicle save(DeliveryVehicle deliveryVehicle);

    DeliveryVehicle findById(int id);

    List<DeliveryVehicle> findAllEmptyVehicles();

    PageResponseDTO<DeliveryVehicleNoDeliveriesDTO> findAll(int pageNo, int pageSize);

    DeliveryVehicleDTO findByDeliveryManUsername(String deliveryManUsername);

    Map<String, Object> getRouteNavigationData(int id);

    Map<String, Object> getRouteElevationData(int id);

    boolean isAssociatedWithDeliveryMan(int id, String deliveryManUsername);

    void leaveVehicle(int id);

}
