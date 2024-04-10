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

@Service
@Transactional
@RequiredArgsConstructor
class DeliveryManServiceImpl extends PagingService<DeliveryManDTO> implements IDeliveryManService {

    private final DeliveryManRepository deliveryManRepository;

    @Override
    public void save(String username) {

        if(deliveryManRepository.findByUsername(username).isPresent()){
            throw new DeliveryManAlreadyExistsException();
        }

        deliveryManRepository.save(new DeliveryMan(username));
    }

    @Override
    public DeliveryMan findByUsername(String username) {
        return deliveryManRepository.findByUsername(username).orElseThrow(
                () -> new DeliveryManNotFoundException(username)
        );
    }

    @Override
    public DeliveryMan findFirstByDeliveryIsNull() {
        return deliveryManRepository.findFirstFreeDeliveryMan().orElseThrow(
                NoFreeDeliveryManFound::new
        );
    }

    @Override
    public PageResponseDTO<DeliveryManDTO> findAllFreeDeliveryMan(int pageNo, int pageSize) {
        return createPageResponse(
                () -> deliveryManRepository.findAllFreeDeliveryMan(PageRequest.of(pageNo, pageSize)));
    }
}
