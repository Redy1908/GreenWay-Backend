package dev.redy1908.greenway.delivery_vehicle.domain;

import dev.redy1908.greenway.app.web.models.PageResponseDTO;
import dev.redy1908.greenway.delivery.domain.Delivery;
import dev.redy1908.greenway.delivery_man.domain.DeliveryMan;
import dev.redy1908.greenway.delivery_vehicle.domain.dto.DeliveryVehicleCreationDTO;
import dev.redy1908.greenway.delivery_vehicle.domain.dto.DeliveryVehicleDTO;
import dev.redy1908.greenway.delivery_vehicle.domain.dto.DeliveryVehicleNoDeliveriesDTO;
import dev.redy1908.greenway.delivery_vehicle.domain.exceptions.models.DeliveryVehicleNotFoundException;
import dev.redy1908.greenway.delivery_vehicle.domain.exceptions.models.NoDeliveryAssignedException;
import dev.redy1908.greenway.delivery_vehicle.domain.exceptions.models.NoDeliveryManAssignedException;
import dev.redy1908.greenway.osrm.domain.IOsrmService;
import dev.redy1908.greenway.osrm.domain.NavigationType;
import dev.redy1908.greenway.vehicle_deposit.domain.IVehicleDepositService;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryVehicleServiceImpl implements IDeliveryVehicleService {

    private final DeliveryVehicleRepository deliveryVehicleRepository;
    private final DeliveryVehicleMapper deliveryVehicleMapper;
    private final IOsrmService osrmService;
    private final IVehicleDepositService vehicleDepositService;

    @Override
    public DeliveryVehicle save(DeliveryVehicleCreationDTO deliveryVehicleCreationDTO) {
        DeliveryVehicle deliveryVehicle = deliveryVehicleMapper.vehicleCreationDTOtoVehicle(deliveryVehicleCreationDTO);
        return deliveryVehicleRepository.save(deliveryVehicle);
    }

    @Override
    public DeliveryVehicle save(DeliveryVehicle deliveryVehicle) {
        return deliveryVehicleRepository.save(deliveryVehicle);
    }

    @Override
    public DeliveryVehicle findById(int id) {
        return deliveryVehicleRepository.findById(id).orElseThrow(() -> new DeliveryVehicleNotFoundException(id));
    }

    @Override
    public List<DeliveryVehicle> findAllEmptyVehicles() {
        return deliveryVehicleRepository.findAllByDeliveriesIsNull();
    }

    @Override
    public PageResponseDTO<DeliveryVehicleNoDeliveriesDTO> findAll(int pageNo, int pageSize) {
        Page<DeliveryVehicle> elements = deliveryVehicleRepository.findAll(PageRequest.of(pageNo, pageSize));
        List<DeliveryVehicle> listElements = elements.getContent();
        List<DeliveryVehicleNoDeliveriesDTO> content = listElements.stream().map(deliveryVehicleMapper::deliveryVehicleToDeliveryVehicleNoDeliveriesDTO).toList();

        PageResponseDTO<DeliveryVehicleNoDeliveriesDTO> pageResponseDTO = new PageResponseDTO<>();
        pageResponseDTO.setContent(content);
        pageResponseDTO.setPageNo(elements.getNumber());
        pageResponseDTO.setPageSize(elements.getSize());
        pageResponseDTO.setTotalElements(elements.getTotalElements());
        pageResponseDTO.setTotalPages(elements.getTotalPages());
        pageResponseDTO.setLast(elements.isLast());

        return pageResponseDTO;
    }

    @Override
    public DeliveryVehicleDTO findByDeliveryManUsername(String deliveryManUsername) {
        DeliveryVehicle deliveryVehicle = deliveryVehicleRepository.findByDeliveryMan_Username(deliveryManUsername).orElseThrow(
                () -> new DeliveryVehicleNotFoundException(deliveryManUsername));

        return deliveryVehicleMapper.deliveryVehicleToDeliveryVehicleDTO(deliveryVehicle);
    }

    @Override
    public Map<String, Object> getRouteNavigationData(int id, NavigationType navigationType) {
        DeliveryVehicle deliveryVehicle = deliveryVehicleRepository.findById(id).orElseThrow(() -> new DeliveryVehicleNotFoundException(id));

        Point startingPoint = vehicleDepositService.getVehicleDeposit().getDepositCoordinates();

        List<Point> wayPoints = extractWaypoints(startingPoint, deliveryVehicle.getDeliveries(), id);

        return osrmService.getNavigationData(startingPoint, wayPoints, navigationType);
    }

    @Override
    public boolean isAssociatedWithDeliveryMan(int vehicleId, String deliveryManUsername) {
        return deliveryVehicleRepository.existsByIdAndDeliveryMan_Username(vehicleId, deliveryManUsername);
    }

    @Override
    public void leaveVehicle(int id) {
       DeliveryVehicle deliveryVehicle = deliveryVehicleRepository.findById(id).orElseThrow(
               () -> new DeliveryVehicleNotFoundException(id));

        DeliveryMan deliveryMan = deliveryVehicle.getDeliveryMan();

        if(deliveryMan == null){
           throw new NoDeliveryManAssignedException(id);
        }

        deliveryVehicle.setDeliveryMan(null);
        deliveryMan.setDeliveryVehicle(null);

        deliveryVehicle.getDeliveries().forEach(delivery -> {
            delivery.setDeliveryVehicle(null);
            deliveryVehicle.setCurrentLoadKg(deliveryVehicle.getCurrentLoadKg() - delivery.getWeightKg());
        });

        deliveryVehicle.setDeliveries(null);

        deliveryVehicleRepository.save(deliveryVehicle);

    }

    private List<Point> extractWaypoints(Point startingPoint, List<Delivery> deliveryList, int id) {

        if (deliveryList.isEmpty()) {
            throw new NoDeliveryAssignedException(id);
        }

        List<Point> wayPoints = deliveryList.stream()
                .map(Delivery::getReceiverCoordinates)
                .collect(Collectors.toList());

        wayPoints.addLast(startingPoint);

        return wayPoints;
    }
}
