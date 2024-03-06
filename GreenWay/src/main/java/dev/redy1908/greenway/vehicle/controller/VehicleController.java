package dev.redy1908.greenway.vehicle.controller;

import dev.redy1908.greenway.vehicle.dto.VehicleDto;
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

    @GetMapping("/{vehicleModel}")
    public ResponseEntity<VehicleDto> getVehicle(@PathVariable String vehicleModel){
        VehicleDto vehicleDto = vehicleService.getVehicle(vehicleModel);
        return ResponseEntity.ok(vehicleDto);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateVehicle(@Valid @RequestBody VehicleDto vehicleDto){
        vehicleService.updateVehicle(vehicleDto);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVehicle(String vehicleModel){
        vehicleService.deleteVehicle(vehicleModel);
    }

}
