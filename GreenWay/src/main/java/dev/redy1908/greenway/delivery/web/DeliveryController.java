package dev.redy1908.greenway.delivery.web;

import dev.redy1908.greenway.app.web.models.PageResponseDTO;
import dev.redy1908.greenway.app.web.models.ResponseDTO;
import dev.redy1908.greenway.delivery.domain.Delivery;
import dev.redy1908.greenway.delivery.domain.DeliveryMapper;
import dev.redy1908.greenway.delivery.domain.IDeliveryService;
import dev.redy1908.greenway.delivery.domain.dto.DeliveryCreationDTO;
import dev.redy1908.greenway.delivery.domain.dto.DeliveryDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/deliveries")
public class DeliveryController {

    private final IDeliveryService deliveryService;
    private final DeliveryMapper deliveryMapper;

    @PostMapping
    public ResponseEntity<ResponseDTO> save(@Valid @RequestBody DeliveryCreationDTO deliveryCreationDTO) {

        Delivery delivery = deliveryMapper.deliveryCreationDTOtoDelivery(deliveryCreationDTO);
        Delivery sacedDelivery = deliveryService.save(delivery);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/v1/deliveries/{deliveryId}")
                .buildAndExpand(sacedDelivery.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(new ResponseDTO(HttpStatus.CREATED.value(), HttpStatus.CREATED, "Delivery created"));
    }

    @GetMapping
    public ResponseEntity<PageResponseDTO<DeliveryDTO>> getAllVehicles(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {

        PageResponseDTO<DeliveryDTO> deliveryPageResponseDTO = deliveryService.findAll(pageNo, pageSize);

        return ResponseEntity.ok().body(deliveryPageResponseDTO);
    }
}
