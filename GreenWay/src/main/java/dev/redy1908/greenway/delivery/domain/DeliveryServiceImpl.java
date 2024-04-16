package dev.redy1908.greenway.delivery.domain;

import dev.redy1908.greenway.delivery.domain.exceptions.model.DeliveryNotFoundException;
import dev.redy1908.greenway.osrm.domain.IOsrmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
class DeliveryServiceImpl implements IDeliveryService {

    private final DeliveryRepository repository;
    private final IOsrmService osrmService;

    @Override
    public Delivery save(Delivery delivery) {
        osrmService.checkPointInBounds(delivery.getReceiverCoordinates());
        return repository.save(delivery);
    }

    @Override
    public Delivery findById(int id) {
        return repository.findById(id).orElseThrow(() -> new DeliveryNotFoundException(id));
    }

    @Override
    public List<Delivery> findAllByDeliveryVehicleNull() {
        return repository.findAllByDeliveryVehicleNull();
    }
}
