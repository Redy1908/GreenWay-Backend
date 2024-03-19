package dev.redy1908.greenway.delivery_man.service;

import dev.redy1908.greenway.delivery_man.model.DeliveryMan;

public interface IDeliveryManService {

    DeliveryMan findByUsername(String username);

}
