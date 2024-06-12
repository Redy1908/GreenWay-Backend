package dev.redy1908.greenway.vehicle_deposit.domain;

import dev.redy1908.greenway.vehicle_deposit.domain.dto.VehicleDepositDTO;

public interface IVehicleDepositService {

    VehicleDeposit saveVehicleDeposit(VehicleDepositDTO vehicleDepositDTO);

    VehicleDeposit getVehicleDeposit();

    void updateVehicleDeposit(VehicleDepositDTO vehicleDepositDTO);
}
