package dev.redy1908.greenway.delivery_man.domain;

import dev.redy1908.greenway.delivery_man.domain.exceptions.models.DeliveryManNotFoundException;
import dev.redy1908.greenway.delivery_man.domain.exceptions.models.NoFreeDeliveryManFound;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
class DeliveryManServiceImpl implements IDeliveryManService {

    private final DeliveryManRepository deliveryManRepository;

    @Override
    public void save(String username) {
        if (deliveryManRepository.findByUsername(username).isEmpty()) {
            deliveryManRepository.save(new DeliveryMan(username));
        }
    }

    @Override
    public DeliveryMan findByUsername(String username) {
        Optional<DeliveryMan> deliveryMan = deliveryManRepository.findByUsername(username);

        if (deliveryMan.isPresent()) {
            return deliveryMan.get();
        }

        throw new DeliveryManNotFoundException(username);
    }

    @Override
    public DeliveryMan findFirstByDeliveryIsNull() {
        return deliveryManRepository.findFirstByDeliveryIsNull().orElseThrow(
                NoFreeDeliveryManFound::new
        );
    }

}
