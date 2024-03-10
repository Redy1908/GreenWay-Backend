package dev.redy1908.greenway.delivery.controller;

import dev.redy1908.greenway.delivery.dto.DeliveryCreationDto;
import dev.redy1908.greenway.delivery.dto.DeliveryDto;
import dev.redy1908.greenway.delivery.service.IDeliveryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("api/v1/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final IDeliveryService deliveryService;

    @PostMapping
    public ResponseEntity<DeliveryDto> crateDelivery(@Valid @RequestBody DeliveryCreationDto deliveryCreationDto){

        DeliveryDto delivery = deliveryService.createDelivery(deliveryCreationDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/v1/deliveries/{deliveryId}")
                .buildAndExpand(delivery.id())
                .toUri();

        return ResponseEntity.created(location).body(delivery);
    }
}
