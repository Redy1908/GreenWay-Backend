package dev.redy1908.greenway.delivery.service.impl;

import dev.redy1908.greenway.delivery.dto.DeliveryCreationDto;
import dev.redy1908.greenway.delivery.dto.DeliveryPageResponseDTO;
import dev.redy1908.greenway.delivery.exceptions.DeliveryNotFoundException;
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
import dev.redy1908.greenway.vehicle.model.Vehicle;
import dev.redy1908.greenway.vehicle.service.IVehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements IDeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final IDeliveryPathService deliveryPathService;
    private final IVehicleService vehicleService;
    private final IDeliveryManService deliveryManService;
    private final IDeliveryPackageService deliveryPackageService;
    private final DeliveryPackageMapper deliveryPackageMapper;

    @Override
    @Transactional
    public Delivery createDelivery(DeliveryCreationDto deliveryCreationDto) {

        DeliveryPath deliveryPath = deliveryPathService.createDeliveryPath(
                deliveryCreationDto.startPoint(),
                deliveryCreationDto.packages().stream().map(
                        deliveryPackage -> new Point(
                                deliveryPackage.destination().latitude(),
                                deliveryPackage.destination().longitude())).collect(Collectors.toList()));

        Vehicle vehicle = vehicleService.getVehicleById(deliveryCreationDto.vehicleId());

        Delivery delivery = new Delivery(deliveryPath, vehicle);

        List<DeliveryPackage> deliveryPackages = deliveryCreationDto.packages().stream().map(deliveryPackageMapper::toEntity).toList();
        deliveryPackages.forEach(deliveryPackage -> deliveryPackage.setDelivery(delivery));
        deliveryPackageService.saveAll(deliveryPackages);

        delivery.setDeliveryPackages(deliveryPackages);

        return deliveryRepository.save(delivery);
    }

    @Override
    public Delivery getDeliveryById(Long deliveryId) {
        return deliveryRepository.findById(deliveryId).orElseThrow(
                () -> new DeliveryNotFoundException(deliveryId)
        );
    }

    @Override
    public DeliveryPageResponseDTO getAllDeliveries(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Delivery> deliveries = deliveryRepository.findAll(pageable);
        List<Delivery> listDeliveries = deliveries.getContent();

        DeliveryPageResponseDTO deliveryPageResponseDTO = new DeliveryPageResponseDTO();
        deliveryPageResponseDTO.setContent(listDeliveries);
        deliveryPageResponseDTO.setPageNo(deliveries.getNumber());
        deliveryPageResponseDTO.setPageSize(deliveries.getSize());
        deliveryPageResponseDTO.setTotalElements(deliveries.getTotalElements());
        deliveryPageResponseDTO.setTotalPages(deliveries.getTotalPages());
        deliveryPageResponseDTO.setLast(deliveries.isLast());

        return deliveryPageResponseDTO;
    }


    @Override
    public DeliveryPageResponseDTO getDeliveriesByDeliveryMan(String deliveryManUsername, int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Delivery> deliveries = deliveryRepository.findAllByDeliveryMan_Username(deliveryManUsername, pageable);
        List<Delivery> listDeliveries = deliveries.getContent();

        DeliveryPageResponseDTO deliveryPageResponseDTO = new DeliveryPageResponseDTO();
        deliveryPageResponseDTO.setContent(listDeliveries);
        deliveryPageResponseDTO.setPageNo(deliveries.getNumber());
        deliveryPageResponseDTO.setPageSize(deliveries.getSize());
        deliveryPageResponseDTO.setTotalElements(deliveries.getTotalElements());
        deliveryPageResponseDTO.setTotalPages(deliveries.getTotalPages());
        deliveryPageResponseDTO.setLast(deliveries.isLast());

        return deliveryPageResponseDTO;
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
}
