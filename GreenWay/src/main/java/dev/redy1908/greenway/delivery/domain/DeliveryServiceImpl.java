package dev.redy1908.greenway.delivery.domain;

import java.util.List;
import java.util.Set;

import dev.redy1908.greenway.delivery_package.domain.dto.DeliveryPackageDTO;
import dev.redy1908.greenway.vehicle.domain.exceptions.models.VehicleAlreadyAssignedException;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.redy1908.greenway.app.web.models.PageResponseDTO;
import dev.redy1908.greenway.delivery.domain.dto.DeliveryCreationDto;
import dev.redy1908.greenway.delivery.domain.dto.DeliveryDTO;
import dev.redy1908.greenway.delivery.domain.exceptions.models.DeliveryNotFoundException;
import dev.redy1908.greenway.delivery_man.domain.DeliveryMan;
import dev.redy1908.greenway.delivery_man.domain.IDeliveryManService;
import dev.redy1908.greenway.delivery_package.domain.DeliveryPackage;
import dev.redy1908.greenway.delivery_package.domain.IDeliveryPackageService;
import dev.redy1908.greenway.delivery_path.domain.DeliveryPath;
import dev.redy1908.greenway.delivery_path.domain.IDeliveryPathService;
import dev.redy1908.greenway.util.services.PagingService;
import dev.redy1908.greenway.vehicle.domain.IVehicleService;
import dev.redy1908.greenway.vehicle.domain.Vehicle;
import dev.redy1908.greenway.vehicle.domain.exceptions.models.VehicleCapacityExceeded;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
class DeliveryServiceImpl extends PagingService<Delivery, DeliveryDTO> implements IDeliveryService {

    private final DeliveryRepository deliveryRepository;

    private final IDeliveryPathService deliveryPathService;
    private final IVehicleService vehicleService;
    private final IDeliveryManService deliveryManService;
    private final IDeliveryPackageService deliveryPackageService;

    private final DeliveryMapper deliveryMapper;

    @Override
    public Delivery createDelivery(DeliveryCreationDto deliveryCreationDto) {

        if (deliveryRepository.existsByVehicle_Id(deliveryCreationDto.vehicleId())) {
            throw new VehicleAlreadyAssignedException();
        }

        DeliveryPath deliveryPath = createDeliveryPath(deliveryCreationDto);
        Vehicle vehicle = vehicleService.findVehicleById(deliveryCreationDto.vehicleId());
        Delivery delivery = new Delivery(deliveryPath, vehicle);

        Set<DeliveryPackage> deliveryPackages = deliveryPackageService
                .setPackagesDelivery(deliveryCreationDto.packages(), delivery);

        double deliveryTotalWeight = deliveryPackageService.calculatePackagesWeight(deliveryPackages);

        if (!vehicleCanCarryAll(vehicle, deliveryTotalWeight)) {
            throw new VehicleCapacityExceeded(vehicle.getMaxCapacity(), deliveryTotalWeight);
        }

        delivery.setDeliveryPackages(deliveryPackages);
        return deliveryRepository.save(delivery);
    }

    @Override
    public DeliveryDTO getDeliveryById(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(
                () -> new DeliveryNotFoundException(deliveryId));

        return deliveryMapper.toDto(delivery);
    }

    @Override
    public PageResponseDTO<DeliveryDTO> getAllDeliveries(int pageNo, int pageSize) {

        return createPageResponse(
                () -> deliveryRepository.findAll(PageRequest.of(pageNo, pageSize)));
    }

    @Override
    public PageResponseDTO<DeliveryDTO> getAllUnassignedDeliveries(int pageNo, int pageSize) {

        return createPageResponse(
                () -> deliveryRepository.findAllByDeliveryManIsNull(PageRequest.of(pageNo, pageSize)));
    }

    @Override
    public boolean isDeliveryOwner(Long deliveryId, String deliveryManUsername) {
        return deliveryRepository.getDeliveryByIdAndDeliveryMan_Username(deliveryId, deliveryManUsername).isPresent();
    }

    @Override
    public void selectDelivery(Long deliveryID, String deliveryManUsername) {
        DeliveryMan deliveryMan = deliveryManService.findByUsername(deliveryManUsername);
        Delivery delivery = deliveryRepository.findById(deliveryID).orElseThrow(
                () -> new DeliveryNotFoundException(deliveryID));

        delivery.setDeliveryMan(deliveryMan);
    }

    protected DeliveryDTO mapToDto(Delivery delivery) {
        return deliveryMapper.toDto(delivery);
    }

    private boolean vehicleCanCarryAll(Vehicle vehicle, double deliveryTotalWeight) {
        return vehicle.getMaxCapacity() >= deliveryTotalWeight;
    }

    private DeliveryPath createDeliveryPath(DeliveryCreationDto deliveryCreationDto) {
        List<Point> points = deliveryCreationDto.packages().stream()
                .map(DeliveryPackageDTO::destination)
                .toList();
        return deliveryPathService.createDeliveryPath(deliveryCreationDto.startPoint(), points);
    }
}
