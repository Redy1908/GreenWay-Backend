package dev.redy1908.greenway.delivery_man.domain;

public interface IDeliveryManService {

    void save(String username);

    DeliveryMan findByUsername(String username);

    DeliveryMan findFirstByDeliveryIsNull();

}
