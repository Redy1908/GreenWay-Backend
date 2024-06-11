package dev.redy1908.greenway.delivery.web;

import dev.redy1908.greenway.app.web.models.PageResponseDTO;
import dev.redy1908.greenway.app.web.models.ResponseDTO;
import dev.redy1908.greenway.delivery.domain.Delivery;
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
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("api/v1/deliveries")
public class DeliveryController {

    private final IDeliveryService deliveryService;

    @PostMapping
    public ResponseEntity<ResponseDTO> save(@Valid @RequestBody DeliveryCreationDTO deliveryCreationDTO) {

        Delivery savedDelivery = deliveryService.save(deliveryCreationDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/v1/deliveries/{deliveryId}")
                .buildAndExpand(savedDelivery.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(new ResponseDTO(HttpStatus.CREATED.value(), HttpStatus.CREATED, "Delivery created"));
    }

    @GetMapping
    public ResponseEntity<PageResponseDTO<DeliveryDTO>> getAllDeliveries(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {

        PageResponseDTO<DeliveryDTO> deliveryPageResponseDTO = deliveryService.findAll(pageNo, pageSize);

        return ResponseEntity.ok().body(deliveryPageResponseDTO);
    }

    @GetMapping("{deliveryId}/complete")
    public ResponseEntity<ResponseDTO> complete(@PathVariable int deliveryId) {
        deliveryService.completeDelivery(deliveryId);
        return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), HttpStatus.OK, "Delivery completed"));
    }
}
