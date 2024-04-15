package dev.redy1908.greenway.delivery.domain;

import dev.redy1908.greenway.delivery.domain.exceptions.model.DeliveryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class DeliveryServiceImpl implements IDeliveryService {

    private final DeliveryRepository repository;

    @Override
    public Delivery save(Delivery delivery) {
        return repository.save(delivery);
    }

    @Override
    public Delivery findById(int id) {
        return repository.findById(id).orElseThrow(() -> new DeliveryNotFoundException(id));
    }
}
