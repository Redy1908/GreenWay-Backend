package dev.redy1908.greenway.delivery_man.domain;

import dev.redy1908.greenway.delivery_man.domain.dto.DeliveryManDTO;
import dev.redy1908.greenway.delivery_man.domain.exceptions.models.DeliveryManAlreadyExistsException;
import dev.redy1908.greenway.util.services.PagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class DeliveryManServiceImpl extends PagingService<DeliveryMan, DeliveryManDTO> implements IDeliveryManService {

    private final DeliveryManRepository deliveryManRepository;
    private final DeliveryManMapper deliveryManMapper;

    @Override
    public DeliveryMan save(String username) {

        if(deliveryManRepository.findByUsername(username).isPresent()){
            throw new DeliveryManAlreadyExistsException();
        }

        return deliveryManRepository.save(new DeliveryMan(username));
    }

    @Override
    protected DeliveryManDTO mapToDto(DeliveryMan entity) {
        return deliveryManMapper.toDTO(entity);
    }
}
