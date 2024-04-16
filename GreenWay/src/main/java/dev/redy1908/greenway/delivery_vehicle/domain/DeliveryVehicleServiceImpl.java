package dev.redy1908.greenway.delivery_vehicle.domain;

import dev.redy1908.greenway.delivery.domain.Delivery;
import dev.redy1908.greenway.delivery_vehicle.domain.dto.DeliveryVehicleCreationDTO;
import dev.redy1908.greenway.delivery_vehicle.domain.dto.DeliveryVehicleDTO;
import dev.redy1908.greenway.delivery_vehicle.domain.exceptions.models.DeliveryVehicleNotFoundException;
import dev.redy1908.greenway.delivery_vehicle.domain.exceptions.models.NoDeliveryAssignedException;
import dev.redy1908.greenway.osrm.domain.IOsrmService;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
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
    public List<DeliveryVehicle> findAll() {
        return deliveryVehicleRepository.findAll();
    }

    @Override
    public DeliveryVehicleDTO findByDeliveryManUsername(String deliveryManUsername) {
        DeliveryVehicle deliveryVehicle = deliveryVehicleRepository.findByDeliveryMan_Username(deliveryManUsername).orElseThrow(
                () -> new DeliveryVehicleNotFoundException(deliveryManUsername));

        return deliveryVehicleMapper.deliveryVehicleToDeliveryVehicleDTO(deliveryVehicle);
    }

    @Override
    public Map<String, Object> getRouteNavigationData(int id) {
        DeliveryVehicle deliveryVehicle = deliveryVehicleRepository.findById(id).orElseThrow(() -> new DeliveryVehicleNotFoundException(id));

        Point startingPoint = deliveryVehicle.getDepositCoordinates();

        List<Point> wayPoints = extractWaypoints(startingPoint, deliveryVehicle.getDeliveries(), id);

        return osrmService.getNavigationData(startingPoint, wayPoints);
    }

    @Override
    public Map<String, Object> getRouteElevationData(int id) {
        DeliveryVehicle deliveryVehicle = deliveryVehicleRepository.findById(id).orElseThrow(() -> new DeliveryVehicleNotFoundException(id));

        Point startingPoint = deliveryVehicle.getDepositCoordinates();

        List<Point> wayPoints = extractWaypoints(startingPoint, deliveryVehicle.getDeliveries(), id);

        return osrmService.getElevationData(startingPoint, wayPoints);
    }

    private List<Point> extractWaypoints(Point startingPoint, List<Delivery> deliveryList, int id) {

        List<Point> wayPoints = deliveryList.stream()
                .map(Delivery::getReceiverCoordinates)
                .collect(Collectors.toList());

        if (wayPoints.isEmpty()) {
            throw new NoDeliveryAssignedException(id);
        }

        wayPoints.addLast(startingPoint);

        return wayPoints;
    }
}
