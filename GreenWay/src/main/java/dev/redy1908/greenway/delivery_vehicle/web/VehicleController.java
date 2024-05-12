package dev.redy1908.greenway.delivery_vehicle.web;

import dev.redy1908.greenway.app.web.models.PageResponseDTO;
import dev.redy1908.greenway.app.web.models.ResponseDTO;
import dev.redy1908.greenway.delivery_vehicle.domain.DeliveryVehicle;
import dev.redy1908.greenway.delivery_vehicle.domain.IDeliveryVehicleService;
import dev.redy1908.greenway.delivery_vehicle.domain.dto.DeliveryVehicleCreationDTO;
import dev.redy1908.greenway.delivery_vehicle.domain.dto.DeliveryVehicleDTO;
import dev.redy1908.greenway.delivery_vehicle.domain.dto.DeliveryVehicleNoDeliveriesDTO;
import dev.redy1908.greenway.osrm.domain.NavigationType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final IDeliveryVehicleService vehicleService;

    @PostMapping
    public ResponseEntity<ResponseDTO> saveVehicle(@RequestBody @Valid DeliveryVehicleCreationDTO deliveryVehicleCreationDTO) {

        DeliveryVehicle savedDeliveryVehicle = vehicleService.save(deliveryVehicleCreationDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/v1/vehicles/{vehicleId}")
                .buildAndExpand(savedDeliveryVehicle.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(new ResponseDTO(HttpStatus.CREATED.value(), HttpStatus.CREATED, "Vehicle created"));
    }

    @GetMapping("/deliveryman/{deliveryManUsername}")
    @PreAuthorize("hasRole('GREEN_WAY_ADMIN') || (#deliveryManUsername == authentication.principal.claims['preferred_username'])")
    public ResponseEntity<DeliveryVehicleDTO> findByDeliveryManUsername(@PathVariable String deliveryManUsername) {
        return ResponseEntity.ok(vehicleService.findByDeliveryManUsername(deliveryManUsername));
    }

    @GetMapping
    public ResponseEntity<PageResponseDTO<DeliveryVehicleNoDeliveriesDTO>> getAllVehicles(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {

        PageResponseDTO<DeliveryVehicleNoDeliveriesDTO> deliveryVehiclePageResponseDTO = vehicleService.findAll(pageNo, pageSize);

        return ResponseEntity.ok().body(deliveryVehiclePageResponseDTO);
    }

    @GetMapping("/{vehicleId}/route")
    @PreAuthorize("hasRole('GREEN_WAY_ADMIN') || @deliveryVehicleServiceImpl.isAssociatedWithDeliveryMan(#vehicleId, authentication.principal.claims['preferred_username'])")
    public ResponseEntity<Map<String, Object>> getRouteNavigationData(@PathVariable int vehicleId, @RequestParam NavigationType navigationType) {
        return ResponseEntity.ok(vehicleService.getRouteNavigationData(vehicleId, navigationType));
    }

    @GetMapping("{vehicleId}/leave")
    @PreAuthorize("@deliveryVehicleServiceImpl.isAssociatedWithDeliveryMan(#vehicleId, authentication.principal.claims['preferred_username'])")
    public ResponseEntity<ResponseDTO> leaveVehicle(@PathVariable int vehicleId) {
        vehicleService.leaveVehicle(vehicleId);
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(), HttpStatus.OK, "Vehicle cleared"));
    }

    @GetMapping("{vehicleId}/enter")
    @PreAuthorize("@deliveryVehicleServiceImpl.isAssociatedWithDeliveryMan(#vehicleId, authentication.principal.claims['preferred_username'])")
    public ResponseEntity<ResponseDTO> enterVehicle(@PathVariable int vehicleId) {
        vehicleService.enterVehicle(vehicleId);
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(), HttpStatus.OK, "OK"));
    }

}
