package dev.redy1908.greenway.vehicle_deposit.web;

import dev.redy1908.greenway.app.web.models.ResponseDTO;
import dev.redy1908.greenway.vehicle_deposit.domain.VehicleDeposit;
import dev.redy1908.greenway.vehicle_deposit.domain.IVehicleDepositService;
import dev.redy1908.greenway.vehicle_deposit.domain.dto.VehicleDepositDTO;
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
@RequestMapping("api/v1/deposit")
public class VehicleDepositController {

    private final IVehicleDepositService depositService;

    @PostMapping
    public ResponseEntity<ResponseDTO> saveVehicleDeposit(@RequestBody @Valid VehicleDepositDTO vehicleDepositDTO){
        VehicleDeposit savedVehicleDeposit = depositService.saveVehicleDeposit(vehicleDepositDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/v1/deposit/{depositId}")
                .buildAndExpand(savedVehicleDeposit.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(new ResponseDTO(HttpStatus.CREATED.value(), HttpStatus.CREATED, "Deposit created"));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void updateVehicleDeposit(@RequestBody @Valid VehicleDepositDTO vehicleDepositDTO){
        depositService.updateVehicleDeposit(vehicleDepositDTO);
    }
}
