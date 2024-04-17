package dev.redy1908.greenway.vehicle_deposit.domain;

import dev.redy1908.greenway.vehicle_deposit.domain.dto.VehicleDepositDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface VehicleDepositMapper {

    VehicleDeposit vehicleDepositDTOtoVehicleDeposit(VehicleDepositDTO vehicleDepositDTO);
}
