package dev.redy1908.greenway.delivery.domain;

import dev.redy1908.greenway.app.web.models.PageResponseDTO;
import dev.redy1908.greenway.delivery.domain.dto.DeliveryCreationDTO;
import dev.redy1908.greenway.delivery.domain.dto.DeliveryDTO;
import dev.redy1908.greenway.delivery.domain.exceptions.model.DeliveryAlreadyCompletedException;
import dev.redy1908.greenway.delivery.domain.exceptions.model.DeliveryNotFoundException;
import dev.redy1908.greenway.delivery.domain.exceptions.model.DeliveryNotInTransitException;
import dev.redy1908.greenway.delivery_vehicle.domain.DeliveryVehicle;
import dev.redy1908.greenway.delivery_vehicle.domain.IDeliveryVehicleService;
import dev.redy1908.greenway.osrm.domain.IOsrmService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
class DeliveryServiceImpl implements IDeliveryService {

    private final DeliveryRepository repository;
    private final IOsrmService osrmService;
    private final IDeliveryVehicleService deliveryVehicleService;
    private final DeliveryMapper deliveryMapper;
    private final DeliveryRepository deliveryRepository;

    @Override
    public Delivery save(DeliveryCreationDTO deliveryCreationDTO) {
        Delivery delivery = deliveryMapper.deliveryCreationDTOtoDelivery(deliveryCreationDTO);
        osrmService.checkPointInBounds(delivery.getReceiverCoordinates());
        return repository.save(delivery);
    }

    @Override
    public Delivery findById(int id) {
        return repository.findById(id).orElseThrow(() -> new DeliveryNotFoundException(id));
    }

    @Override
    public PageResponseDTO<DeliveryDTO> findAll(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("createdAt").ascending());
        Page<Delivery> elements = repository.findAll(pageable);
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
    public void completeDelivery(int id) {

        Delivery delivery = deliveryRepository.findById(id).orElseThrow(
                () -> new DeliveryNotFoundException(id)
        );

        if(delivery.getDeliveryTime() != null){
            throw new DeliveryAlreadyCompletedException(id);
        }

        if(!delivery.isInTransit()){
            throw new DeliveryNotInTransitException(id);
        }

        DeliveryVehicle deliveryVehicle = deliveryVehicleService.findById(delivery.getDeliveryVehicle().getId());

        delivery.setDeliveryTime(LocalDateTime.now());
        delivery.setDeliveryVehicle(null);
        delivery.setInTransit(false);
        deliveryVehicle.getDeliveries().remove(delivery);
        deliveryVehicle.setCurrentLoadKg(deliveryVehicle.getCurrentLoadKg() - delivery.getWeightKg());

        deliveryRepository.save(delivery);
        deliveryVehicleService.save(deliveryVehicle);
    }

    @Override
    public List<Delivery> findUnassignedDeliveries() {
        return repository.findAllByDeliveryVehicleNullAndDeliveryTimeIsNull();
    }
}
