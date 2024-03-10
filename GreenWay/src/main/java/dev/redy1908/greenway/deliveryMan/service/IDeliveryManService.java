package dev.redy1908.greenway.deliveryMan.service;

import dev.redy1908.greenway.deliveryMan.model.DeliveryMan;

public interface IDeliveryManService {

    DeliveryMan findByUsername(String username);

}
