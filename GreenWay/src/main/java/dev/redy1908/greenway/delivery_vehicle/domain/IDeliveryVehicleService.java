package dev.redy1908.greenway.delivery_vehicle.domain;

import dev.redy1908.greenway.delivery_vehicle.domain.dto.DeliveryVehicleCreationDTO;
import java.util.List;

public interface IDeliveryVehicleService {

    DeliveryVehicle save(DeliveryVehicleCreationDTO deliveryVehicleCreationDTO);

    DeliveryVehicle save(DeliveryVehicle deliveryVehicle);

    DeliveryVehicle findById(int id);

    List<DeliveryVehicle> findAll();

}
