package dev.redy1908.greenway.delivery_vehicle.web;

import dev.redy1908.greenway.app.web.models.ResponseDTO;
import dev.redy1908.greenway.delivery_vehicle.domain.DeliveryVehicle;
import dev.redy1908.greenway.delivery_vehicle.domain.IDeliveryVehicleService;
import dev.redy1908.greenway.delivery_vehicle.domain.dto.DeliveryVehicleCreationDTO;
import dev.redy1908.greenway.delivery_vehicle.domain.dto.DeliveryVehicleDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public ResponseEntity<DeliveryVehicleDTO> findByDeliveryManUsername(@RequestParam(name = "deliveryman") String deliveryManUsername) {
        return ResponseEntity.ok(vehicleService.findByDeliveryManUsername(deliveryManUsername));
    }

    @GetMapping("{vehicleId}/route")
    public ResponseEntity<Map<String, Object>> getRouteNavigationData(@PathVariable int vehicleId) {
        return ResponseEntity.ok(vehicleService.getRouteNavigationData(vehicleId));
    }

    @GetMapping("{vehicleId}/route/elevation")
    public ResponseEntity<Map<String, Object>> getRouteElevationData(@PathVariable int vehicleId) {
        return ResponseEntity.ok(vehicleService.getRouteElevationData(vehicleId));
    }

}
