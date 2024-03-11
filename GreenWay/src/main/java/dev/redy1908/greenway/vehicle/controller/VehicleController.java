package dev.redy1908.greenway.vehicle.controller;

import dev.redy1908.greenway.vehicle.dto.VehicleDto;
import dev.redy1908.greenway.vehicle.dto.VehiclePageResponseDTO;
import dev.redy1908.greenway.vehicle.service.IVehicleService;
import dev.redy1908.greenway.web.model.ResponseDto;
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
    public ResponseEntity<ResponseDto> saveVehicle(@Valid @RequestBody VehicleDto vehicleDto){
        vehicleService.saveVehicle(vehicleDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/v1/vehicles/{vehicleId}")
                .buildAndExpand(vehicleDto.model())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(new ResponseDto(HttpStatus.CREATED.value(), HttpStatus.CREATED, "Vehicle created"));
    }

    @GetMapping("/{vehicleId}")
    public ResponseEntity<VehicleDto> getVehicle(@PathVariable Long vehicleId){
        VehicleDto vehicleDto = vehicleService.getVehicleById(vehicleId);
        return ResponseEntity.ok(vehicleDto);
    }

    @GetMapping
    public ResponseEntity<VehiclePageResponseDTO> getAllVehicles(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ){

        VehiclePageResponseDTO vehiclePageResponseDTO = vehicleService.getAllVehicles(pageNo, pageSize);

        return ResponseEntity.ok().body(vehiclePageResponseDTO);
    }

    @GetMapping("/free")
    public ResponseEntity<VehiclePageResponseDTO> getFreeVehicles(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ){

        VehiclePageResponseDTO vehiclePageResponseDTO = vehicleService.getFreeVehicles(pageNo, pageSize);

        return ResponseEntity.ok().body(vehiclePageResponseDTO);
    }

}
