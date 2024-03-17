package dev.redy1908.greenway.delivery.service.impl;

import dev.redy1908.greenway.app.common.service.PagingService;
import dev.redy1908.greenway.delivery.dto.DeliveryCreationDto;
import dev.redy1908.greenway.delivery.dto.DeliveryDTO;
import dev.redy1908.greenway.delivery.exceptions.DeliveryNotFoundException;
import dev.redy1908.greenway.delivery.exceptions.VehicleCapacityExceeded;
import dev.redy1908.greenway.delivery.mapper.DeliveryMapper;
import dev.redy1908.greenway.delivery.model.Delivery;
import dev.redy1908.greenway.delivery.repository.DeliveryRepository;
import dev.redy1908.greenway.delivery.service.IDeliveryService;
import dev.redy1908.greenway.deliveryMan.model.DeliveryMan;
import dev.redy1908.greenway.deliveryMan.service.IDeliveryManService;
import dev.redy1908.greenway.deliveryPackage.mapper.DeliveryPackageMapper;
import dev.redy1908.greenway.deliveryPackage.model.DeliveryPackage;
import dev.redy1908.greenway.deliveryPackage.service.IDeliveryPackageService;
import dev.redy1908.greenway.deliveryPath.model.DeliveryPath;
import dev.redy1908.greenway.deliveryPath.service.IDeliveryPathService;
import dev.redy1908.greenway.point.Point;
import dev.redy1908.greenway.vehicle.mapper.VehicleMapper;
import dev.redy1908.greenway.vehicle.model.Vehicle;
import dev.redy1908.greenway.vehicle.service.IVehicleService;
import dev.redy1908.greenway.web.model.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl extends PagingService<Delivery, DeliveryDTO> implements IDeliveryService {

    private final DeliveryRepository deliveryRepository;

    private final IDeliveryPathService deliveryPathService;
    private final IVehicleService vehicleService;
    private final IDeliveryManService deliveryManService;
    private final IDeliveryPackageService deliveryPackageService;

    private final DeliveryPackageMapper deliveryPackageMapper;
    private final DeliveryMapper deliveryMapper;
    private final VehicleMapper vehicleMapper;

    @Override
    @Transactional
    public Delivery createDelivery(DeliveryCreationDto deliveryCreationDto) {
        DeliveryPath deliveryPath = createDeliveryPath(deliveryCreationDto);
        Vehicle vehicle = getVehicle(deliveryCreationDto);
        Delivery delivery = new Delivery(deliveryPath, vehicle);
        List<DeliveryPackage> deliveryPackages = createDeliveryPackages(deliveryCreationDto, delivery);
        double deliveryTotalWeight = getDeliveryTotalWeight(deliveryPackages);

        if(!vehicleCanCarryAll(vehicle, deliveryTotalWeight)){
            throw new VehicleCapacityExceeded(vehicle.getMaxCapacity(), deliveryTotalWeight);
        }

        delivery.setDeliveryPackages(deliveryPackages);
        return deliveryRepository.save(delivery);
    }

    @Override
    public DeliveryDTO getDeliveryById(Long deliveryId) {
        Delivery delivery =  deliveryRepository.findById(deliveryId).orElseThrow(
                () -> new DeliveryNotFoundException(deliveryId)
        );

        return deliveryMapper.toDto(delivery);
    }

    @Override
    public PageResponseDTO<DeliveryDTO> getAllDeliveries(int pageNo, int pageSize) {

        return createPageResponse(
                () -> deliveryRepository.findAll(PageRequest.of(pageNo, pageSize))
        );
    }


    @Override
    public PageResponseDTO<DeliveryDTO> getDeliveriesByDeliveryMan(String deliveryManUsername, int pageNo, int pageSize) {

        return createPageResponse(
                () -> deliveryRepository.findAllByDeliveryMan_Username(deliveryManUsername, PageRequest.of(pageNo, pageSize))
        );
    }

    @Override
    public boolean isDeliveryOwner(Long deliveryId, String deliveryManUsername) {
        return deliveryRepository.getDeliveryByIdAndDeliveryMan_Username(deliveryId, deliveryManUsername).isPresent();
    }

    @Override
    @Transactional
    public void selectDelivery(Long deliveryID, String deliveryManUsername) {
        DeliveryMan deliveryMan = deliveryManService.findByUsername(deliveryManUsername);
        Delivery delivery = deliveryRepository.findById(deliveryID).orElseThrow(
                () -> new DeliveryNotFoundException(deliveryID)
        );

        delivery.setDeliveryMan(deliveryMan);
    }

    @Override
    protected DeliveryDTO mapToDto(Delivery delivery) {
        return deliveryMapper.toDto(delivery);
    }

    private double getDeliveryTotalWeight(List<DeliveryPackage> deliveryPackages){
        return deliveryPackages.stream()
                .mapToDouble(DeliveryPackage::getWeight)
                .sum();
    }

    private boolean vehicleCanCarryAll(Vehicle vehicle, double deliveryTotalWeight) {
        return vehicle.getMaxCapacity() >= deliveryTotalWeight;
    }

    private DeliveryPath createDeliveryPath(DeliveryCreationDto deliveryCreationDto) {
        List<Point> points = deliveryCreationDto.packages().stream()
                .map(deliveryPackage -> new Point(deliveryPackage.destination().latitude(), deliveryPackage.destination().longitude()))
                .collect(Collectors.toList());
        return deliveryPathService.createDeliveryPath(deliveryCreationDto.startPoint(), points);
    }

    private Vehicle getVehicle(DeliveryCreationDto deliveryCreationDto) {
        return vehicleMapper.toEntity(vehicleService.getVehicleById(deliveryCreationDto.vehicleId()));
    }

    private List<DeliveryPackage> createDeliveryPackages(DeliveryCreationDto deliveryCreationDto, Delivery delivery) {
        List<DeliveryPackage> deliveryPackages = deliveryCreationDto.packages().stream()
                .map(deliveryPackageMapper::toEntity)
                .toList();
        deliveryPackages.forEach(deliveryPackage -> deliveryPackage.setDelivery(delivery));
        deliveryPackageService.saveAll(deliveryPackages);
        return deliveryPackages;
    }
}
