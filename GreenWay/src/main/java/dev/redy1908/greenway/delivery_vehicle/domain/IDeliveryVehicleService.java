package dev.redy1908.greenway.delivery_vehicle.domain;

import dev.redy1908.greenway.delivery_vehicle.domain.dto.DeliveryVehicleCreationDTO;
import dev.redy1908.greenway.delivery_vehicle.domain.dto.DeliveryVehicleDTO;

import java.util.Map;

public interface IDeliveryVehicleService {

    DeliveryVehicle save(DeliveryVehicleCreationDTO deliveryVehicleCreationDTO);

    DeliveryVehicle save(DeliveryVehicle deliveryVehicle);

    DeliveryVehicle findById(int id);

    DeliveryVehicleDTO findByDeliveryManUsername(String deliveryManUsername);

    Map<String, Object> getRouteNavigationData(int id);

    Map<String, Object> getRouteElevationData(int id);

}
