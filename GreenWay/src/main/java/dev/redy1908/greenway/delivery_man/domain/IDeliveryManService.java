package dev.redy1908.greenway.delivery_man.domain;

import java.util.List;

public interface IDeliveryManService {

    DeliveryMan save(String username);

    List<DeliveryMan> findAllByDeliveryVehicleNull();

}
