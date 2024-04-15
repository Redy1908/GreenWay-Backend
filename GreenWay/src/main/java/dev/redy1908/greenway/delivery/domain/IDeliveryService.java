package dev.redy1908.greenway.delivery.domain;


public interface IDeliveryService {

    Delivery save(Delivery delivery);

    Delivery findById(int id);

}
