package dev.redy1908.greenway.delivery_man.service.impl;

import dev.redy1908.greenway.delivery_man.exceptions.DeliveryManNotFoundException;
import dev.redy1908.greenway.delivery_man.model.DeliveryMan;
import dev.redy1908.greenway.delivery_man.repository.DeliveryManRepository;
import dev.redy1908.greenway.delivery_man.service.IDeliveryManService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeliveryManService implements IDeliveryManService {

    private final DeliveryManRepository deliveryManRepository;

    @Override
    public DeliveryMan findByUsername(String username) {
        Optional<DeliveryMan> deliveryMan = deliveryManRepository.findByUsername(username);

        if(deliveryMan.isPresent()){
            return deliveryMan.get();
        }

        throw new DeliveryManNotFoundException(username);
    }
}
