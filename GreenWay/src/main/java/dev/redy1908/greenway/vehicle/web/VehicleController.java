package dev.redy1908.greenway.vehicle.web;

import dev.redy1908.greenway.app.web.models.PageResponseDTO;
import dev.redy1908.greenway.app.web.models.ResponseDTO;
import dev.redy1908.greenway.vehicle.domain.IVehicleService;
import dev.redy1908.greenway.vehicle.domain.Vehicle;
import dev.redy1908.greenway.vehicle.domain.dto.VehicleDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final IVehicleService vehicleService;

    @PostMapping
    public ResponseEntity<ResponseDTO> saveVehicle(@RequestBody @Valid VehicleDTO vehicleDTO) {
        Vehicle savedVehicle = vehicleService.saveVehicle(vehicleDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/v1/vehicles/{vehicleId}")
                .buildAndExpand(savedVehicle.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(new ResponseDTO(HttpStatus.CREATED.value(), HttpStatus.CREATED, "Vehicle created"));
    }

    @GetMapping("/{vehicleId}")
    public ResponseEntity<VehicleDTO> getVehicle(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(vehicleService.getVehicleById(vehicleId));
    }

    @GetMapping
    public ResponseEntity<PageResponseDTO<VehicleDTO>> getAllVehicles(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {

        PageResponseDTO<VehicleDTO> vehiclePageResponseDTO = vehicleService.getAllVehicles(pageNo, pageSize);

        return ResponseEntity.ok().body(vehiclePageResponseDTO);
    }

}
