package dev.redy1908.greenway.delivery_man.domain;

import dev.redy1908.greenway.delivery_man.domain.exceptions.models.DeliveryManAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
class DeliveryManServiceImpl implements IDeliveryManService {

    private final DeliveryManRepository deliveryManRepository;

    @Override
    public DeliveryMan save(String username) {

        if (deliveryManRepository.findByUsername(username).isPresent()) {
            throw new DeliveryManAlreadyExistsException();
        }

        return deliveryManRepository.save(new DeliveryMan(username));
    }

    @Override
    public List<DeliveryMan> findAllByDeliveryVehicleNull() {
        return deliveryManRepository.findAllByDeliveryVehicleNull();
    }

}
