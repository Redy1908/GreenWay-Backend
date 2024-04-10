package dev.redy1908.greenway.delivery_man.domain;

import dev.redy1908.greenway.app.web.models.PageResponseDTO;
import dev.redy1908.greenway.delivery_man.domain.dto.DeliveryManDTO;
import dev.redy1908.greenway.delivery_man.domain.exceptions.models.DeliveryManAlreadyExistsException;
import dev.redy1908.greenway.delivery_man.domain.exceptions.models.DeliveryManNotFoundException;
import dev.redy1908.greenway.delivery_man.domain.exceptions.models.NoFreeDeliveryManFound;
import dev.redy1908.greenway.util.services.PagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
class DeliveryManServiceImpl extends PagingService<DeliveryMan, DeliveryManDTO> implements IDeliveryManService {

    private final DeliveryManRepository deliveryManRepository;
    private final DeliveryManMapper deliveryManMapper;

    @Override
    public void save(String username) {

        if(deliveryManRepository.findByUsername(username).isPresent()){
            throw new DeliveryManAlreadyExistsException();
        }

        deliveryManRepository.save(new DeliveryMan(username));
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

    @Override
    public PageResponseDTO<DeliveryManDTO> findAllFreeDeliveryMan(int pageNo, int pageSize) {
        return createPageResponse(
                () -> deliveryManRepository.findAllByDeliveryIsNull(PageRequest.of(pageNo, pageSize)));
    }

    @Override
    protected DeliveryManDTO mapToDto(DeliveryMan entity) {
        return deliveryManMapper.toDTO(entity);
    }
}
