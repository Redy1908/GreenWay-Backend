package dev.redy1908.greenway.delivery.domain;

import dev.redy1908.greenway.app.web.models.PageResponseDTO;
import dev.redy1908.greenway.delivery.domain.dto.DeliveryDTO;
import dev.redy1908.greenway.delivery.domain.dto.DeliveryWithNavigationDTO;
import dev.redy1908.greenway.delivery.domain.exceptions.models.DeliveryNotFoundException;
import dev.redy1908.greenway.delivery_man.domain.DeliveryMan;
import dev.redy1908.greenway.delivery_man.domain.IDeliveryManService;
import dev.redy1908.greenway.delivery_package.domain.DeliveryPackage;
import dev.redy1908.greenway.delivery_package.domain.DeliveryPackageMapper;
import dev.redy1908.greenway.delivery_package.domain.IDeliveryPackageService;
import dev.redy1908.greenway.delivery_package.domain.dto.DeliveryPackageDTO;
import dev.redy1908.greenway.osrm.domain.IOsrmService;
import dev.redy1908.greenway.osrm.domain.NavigationType;
import dev.redy1908.greenway.osrm.domain.RequestType;
import dev.redy1908.greenway.util.services.PagingService;
import dev.redy1908.greenway.vehicle.domain.IVehicleService;
import dev.redy1908.greenway.vehicle.domain.Vehicle;
import dev.redy1908.greenway.vehicle.domain.exceptions.models.VehicleAutonomyNotSufficientException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
class DeliveryServiceImpl extends PagingService<Delivery, DeliveryDTO> implements IDeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final IVehicleService vehicleService;
    private final IDeliveryManService deliveryManService;
    private final IDeliveryPackageService deliveryPackageService;
    private final IOsrmService osrmService;
    private final DeliveryMapper deliveryMapper;
    private final DeliveryPackageMapper deliveryPackageMapper;

    @Override
    public Delivery createDelivery(@Valid DeliveryDTO deliveryCreationDTO) {

        osrmService.checkPointBounds(deliveryCreationDTO.depositCoordinates());

        Delivery delivery = new Delivery();
        delivery.setDeliveryDate(deliveryCreationDTO.estimatedDeliveryDate());
        delivery.setDepositAddress(deliveryCreationDTO.depositAddress());
        delivery.setDepositCoordinates(deliveryCreationDTO.depositCoordinates());

        assignDeliveryPackages(delivery, deliveryCreationDTO.deliveryPackages());
        assignVehicle(delivery, deliveryCreationDTO.vehicleId(), getTripsDistances(deliveryCreationDTO));
        assignDeliveryMan(delivery, deliveryCreationDTO.deliveryManUsername());

        return deliveryRepository.save(delivery);
    }

    @Override
    public DeliveryWithNavigationDTO getDeliveryWithNavigationById(Long deliveryId, NavigationType navigationType, RequestType requestType) {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(
                () -> new DeliveryNotFoundException(deliveryId));

        double maxDistance = delivery.getVehicle().getMaxAutonomyKm();

        DeliveryDTO deliveryDTO = deliveryMapper.toDto(delivery);

        Set<Point> wayPoints = extractWaypoints(deliveryDTO.deliveryPackages());
        Map<String, Object> navigationData = osrmService.getNavigationData(deliveryDTO.depositCoordinates(), maxDistance, wayPoints, navigationType, requestType);

        return new DeliveryWithNavigationDTO(deliveryDTO, navigationData);
    }

    @Override
    public Map<String, Object> getDeliveryElevationDataById(Long deliveryId, NavigationType navigationType, RequestType requestType) {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(
                () -> new DeliveryNotFoundException(deliveryId));

        double maxDistance = delivery.getVehicle().getMaxAutonomyKm();

        DeliveryDTO deliveryDTO = deliveryMapper.toDto(delivery);

        Set<Point> wayPoints = extractWaypoints(deliveryDTO.deliveryPackages());
        return osrmService.getElevationData(deliveryDTO.depositCoordinates(), maxDistance, wayPoints, navigationType, requestType);
    }


    @Override
    public PageResponseDTO<DeliveryDTO> getAllDeliveries(int pageNo, int pageSize) {

        return createPageResponse(
                () -> deliveryRepository.findAll(PageRequest.of(pageNo, pageSize)));
    }

    @Override
    public PageResponseDTO<DeliveryDTO> getAllDeliveriesByDeliveryMan(String deliveryManUsername, int pageNo, int pageSize) {

        return createPageResponse(
                () -> deliveryRepository.findAllByDeliveryMan_Username(deliveryManUsername, PageRequest.of(pageNo, pageSize)));
    }

    @Override
    public boolean isDeliveryOwner(Long deliveryId, String deliveryManUsername) {
        return deliveryRepository.findDeliveryDTOByIdAndDeliveryMan_Username(deliveryId, deliveryManUsername).isPresent();
    }

    private List<Double> getTripsDistances(DeliveryDTO deliveryDTO){

        Point depositCoordinates = deliveryDTO.depositCoordinates();
        Set<Point> waypoints = extractWaypoints(deliveryDTO.deliveryPackages());

        double tripDistanceDistance = osrmService.getTripLength(
                depositCoordinates,
                waypoints,
                NavigationType.DISTANCE,
                RequestType.DISTANCE
        );


        double tripDurationDistance = osrmService.getTripLength(
                depositCoordinates,
                waypoints,
                NavigationType.DURATION,
                RequestType.DISTANCE
        );


        double tripElevationDistance = osrmService.getTripLength(
                depositCoordinates,
                waypoints,
                NavigationType.ELEVATION,
                RequestType.DISTANCE
        );


        double tripStandardDistance = osrmService.getTripLength(
                depositCoordinates,
                waypoints,
                NavigationType.STANDARD,
                RequestType.DISTANCE
        );

        return List.of(tripDistanceDistance, tripDurationDistance, tripElevationDistance, tripStandardDistance);
    }

    private Set<Point> extractWaypoints(Set<DeliveryPackageDTO> deliveryPackageDTOS) {

        return deliveryPackageDTOS.stream()
                .map(DeliveryPackageDTO::receiverCoordinates)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private void assignVehicle(Delivery delivery, Long vehicleId, List<Double> tripDistances) {

        Vehicle vehicle = vehicleService.getVehicleIfFree(vehicleId);

        boolean canNavigate = tripDistances.stream()
                .anyMatch(distance -> distance / 1000 <= vehicle.getMaxAutonomyKm());

        if (!canNavigate) {
            throw new VehicleAutonomyNotSufficientException(vehicle.getMaxAutonomyKm(), tripDistances);
        }


        double deliveryTotalWeight = deliveryPackageService.calculatePackagesWeight(delivery.getDeliveryPackages());
        vehicleService.vehicleCapacitySufficientOrThrow(vehicle, deliveryTotalWeight);

        delivery.setVehicle(vehicle);
    }

    private void assignDeliveryPackages(Delivery delivery, Set<DeliveryPackageDTO> packages) {
        Set<DeliveryPackage> deliveryPackages = packages.stream()
                .map(deliveryPackageMapper::toEntity)
                .peek(dP -> osrmService.checkPointBounds(dP.getReceiverCoordinates()))
                .collect(Collectors.toSet());

        deliveryPackages.forEach(deliveryPackage -> deliveryPackage.setDelivery(delivery));
        delivery.setDeliveryPackages(deliveryPackages);
    }

    private void assignDeliveryMan(Delivery delivery, String deliveryManUsername) {

        DeliveryMan deliveryMan;

        if (deliveryManUsername == null) {
            deliveryMan = deliveryManService.findFirstByDeliveryIsNull();
        } else {
            deliveryMan = deliveryManService.findByUsername(deliveryManUsername);
        }

        delivery.setDeliveryMan(deliveryMan);
    }

    @Override
    protected DeliveryDTO mapToDto(Delivery entity) {
        return deliveryMapper.toDto(entity);
    }
}
