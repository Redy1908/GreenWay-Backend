package dev.redy1908.greenway.delivery.domain;

import java.util.List;

public interface IDeliveryService {

    Delivery save(Delivery delivery);

    Delivery findById(int id);

    List<Delivery> findAllByDeliveryVehicleNull();

}
