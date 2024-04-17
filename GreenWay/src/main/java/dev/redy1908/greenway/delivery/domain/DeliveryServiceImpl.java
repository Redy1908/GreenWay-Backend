package dev.redy1908.greenway.delivery.domain;

import dev.redy1908.greenway.app.web.models.PageResponseDTO;
import dev.redy1908.greenway.delivery.domain.dto.DeliveryDTO;
import dev.redy1908.greenway.delivery.domain.exceptions.model.DeliveryNotFoundException;
import dev.redy1908.greenway.osrm.domain.IOsrmService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
class DeliveryServiceImpl implements IDeliveryService {

    private final DeliveryRepository repository;
    private final IOsrmService osrmService;
    private final DeliveryMapper deliveryMapper;

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
    public PageResponseDTO<DeliveryDTO> findAll(int pageNo, int pageSize) {
        Page<Delivery> elements = repository.findAll(PageRequest.of(pageNo, pageSize));
        List<Delivery> listElements = elements.getContent();
        List<DeliveryDTO> content = listElements.stream().map(deliveryMapper::deliveryToDeliverDTO).toList();

        PageResponseDTO<DeliveryDTO> pageResponseDTO = new PageResponseDTO<>();
        pageResponseDTO.setContent(content);
        pageResponseDTO.setPageNo(elements.getNumber());
        pageResponseDTO.setPageSize(elements.getSize());
        pageResponseDTO.setTotalElements(elements.getTotalElements());
        pageResponseDTO.setTotalPages(elements.getTotalPages());
        pageResponseDTO.setLast(elements.isLast());

        return pageResponseDTO;
    }

    @Override
    public List<Delivery> findAllByDeliveryVehicleNull() {
        return repository.findAllByDeliveryVehicleNull();
    }
}
