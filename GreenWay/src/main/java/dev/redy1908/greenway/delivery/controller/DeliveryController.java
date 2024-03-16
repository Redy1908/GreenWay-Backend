package dev.redy1908.greenway.delivery.controller;

import dev.redy1908.greenway.delivery.dto.DeliveryCreationDto;
import dev.redy1908.greenway.delivery.dto.DeliveryPageResponseDTO;
import dev.redy1908.greenway.delivery.model.Delivery;
import dev.redy1908.greenway.delivery.service.IDeliveryService;
import dev.redy1908.greenway.web.model.ResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("api/v1/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final IDeliveryService deliveryService;

    @PostMapping
    public ResponseEntity<ResponseDto> crateDelivery(@Valid @RequestBody DeliveryCreationDto deliveryCreationDto){

        Delivery delivery = deliveryService.createDelivery(deliveryCreationDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/v1/deliveries/{deliveryId}")
                .buildAndExpand(delivery.getId())
                .toUri();

        return ResponseEntity.created(location).body(
                new ResponseDto(
                        HttpStatus.OK.value(),
                        HttpStatus.OK, "New delivery created")
        );
    }

    @GetMapping("/id/{deliveryId}")
    @PreAuthorize("hasRole('GREEN_WAY_ADMIN') || @deliveryServiceImpl.isDeliveryOwner(#deliveryId, authentication.principal.claims['preferred_username'])")
    public ResponseEntity<Delivery> getDeliveryById(@PathVariable Long deliveryId){
        return ResponseEntity.ok(deliveryService.getDeliveryById(deliveryId));
    }

    @GetMapping
    public ResponseEntity<DeliveryPageResponseDTO> getAllDeliveries(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ){

        DeliveryPageResponseDTO deliveryPageResponseDTO = deliveryService.getAllDeliveries(pageNo, pageSize);

        return ResponseEntity.ok().body(deliveryPageResponseDTO);
    }

    @GetMapping("deliveryMan/{deliveryManUsername}")
    public ResponseEntity<DeliveryPageResponseDTO> getDeliveriesByDeliveryMan(
            @PathVariable String deliveryManUsername,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ){

        DeliveryPageResponseDTO deliveryPageResponseDTO = deliveryService.getDeliveriesByDeliveryMan(deliveryManUsername, pageNo, pageSize);

        return ResponseEntity.ok().body(deliveryPageResponseDTO);
    }

    @PutMapping("/select/{deliveryId}")
    public ResponseEntity<ResponseDto> selectDelivery(@PathVariable Long deliveryId, @RequestParam String deliveryMan){
        deliveryService.selectDelivery(deliveryId, deliveryMan);

        return ResponseEntity.ok().body(
                new ResponseDto(
                        HttpStatus.OK.value(),
                        HttpStatus.OK, "Delivery assigned"));
    }
}
