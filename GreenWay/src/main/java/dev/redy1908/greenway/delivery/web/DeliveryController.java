package dev.redy1908.greenway.delivery.web;

import dev.redy1908.greenway.app.web.models.PageResponseDTO;
import dev.redy1908.greenway.app.web.models.ResponseDTO;
import dev.redy1908.greenway.delivery.domain.Delivery;
import dev.redy1908.greenway.delivery.domain.IDeliveryService;
import dev.redy1908.greenway.delivery.domain.dto.DeliveryDTO;
import dev.redy1908.greenway.delivery.domain.dto.DeliveryWithNavigationDTO;
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
class DeliveryController {

    private final IDeliveryService deliveryService;

    @PostMapping
    public ResponseEntity<ResponseDTO> createDelivery(@Valid @RequestBody DeliveryDTO deliveryDTO) {

        Delivery delivery = deliveryService.createDelivery(deliveryDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/v1/deliveries/{deliveryId}")
                .buildAndExpand(delivery.getId())
                .toUri();

        return ResponseEntity.created(location).body(
                new ResponseDTO(
                        HttpStatus.OK.value(),
                        HttpStatus.OK, "New delivery created"));
    }

    @GetMapping("/id/{deliveryId}")
    @PreAuthorize("hasRole('GREEN_WAY_ADMIN') || @deliveryServiceImpl.isDeliveryOwner(#deliveryId, authentication.principal.claims['preferred_username'])")
    public ResponseEntity<DeliveryWithNavigationDTO> getDeliveryById(@PathVariable Long deliveryId, @RequestParam String navigationType) {

        DeliveryWithNavigationDTO deliveryWithNavigationDTO = deliveryService.getDeliveryByIdWithNavigation(deliveryId, navigationType);

        return ResponseEntity.ok(deliveryWithNavigationDTO);
    }

    @GetMapping
    public ResponseEntity<PageResponseDTO<DeliveryDTO>> getAllDeliveries(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {

        PageResponseDTO<DeliveryDTO> deliveryDTO = deliveryService.getAllDeliveries(pageNo, pageSize);

        return ResponseEntity.ok().body(deliveryDTO);
    }

    @GetMapping("/deliveryman/{deliveryManUsername}")
    @PreAuthorize("hasRole('GREEN_WAY_ADMIN') || (#deliveryManUsername == authentication.principal.claims['preferred_username'])")
    public ResponseEntity<PageResponseDTO<DeliveryDTO>> getAllDeliveriesByDeliveryMan(
            @PathVariable String deliveryManUsername,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {

        PageResponseDTO<DeliveryDTO> deliveryPageResponseDTO = deliveryService.getAllDeliveriesByDeliveryMan(deliveryManUsername, pageNo,
                pageSize);

        return ResponseEntity.ok().body(deliveryPageResponseDTO);
    }
}
