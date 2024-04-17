package dev.redy1908.greenway.vehicle_deposit.domain;

import dev.redy1908.greenway.vehicle_deposit.domain.dto.VehicleDepositDTO;
import dev.redy1908.greenway.vehicle_deposit.domain.exceptions.models.VehicleDepositAlreadyExistsException;
import dev.redy1908.greenway.vehicle_deposit.domain.exceptions.models.VehicleDepositNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehicleDepositServiceImpl implements IVehicleDepositService {

    private final VehicleDepositRepository repository;
    private final VehicleDepositMapper vehicleDepositMapper;

    @Override
    public VehicleDeposit saveVehicleDeposit(VehicleDepositDTO vehicleDepositDTO) {

        if(repository.findAll().size() == 1){
            throw new VehicleDepositAlreadyExistsException();
        }

        VehicleDeposit vehicleDeposit = vehicleDepositMapper.vehicleDepositDTOtoVehicleDeposit(vehicleDepositDTO);

        return repository.save(vehicleDeposit);
    }

    @Override
    public VehicleDeposit getVehicleDeposit() {
        return repository.findById(1).orElseThrow(VehicleDepositNotFoundException::new);
    }

    @Override
    public void updateVehicleDeposit(VehicleDepositDTO vehicleDepositDTO) {

        VehicleDeposit savedVehicleDeposit = repository.findById(1).orElseThrow();

        savedVehicleDeposit.setDepositAddress(vehicleDepositDTO.depositAddress());
        savedVehicleDeposit.setDepositCoordinates(vehicleDepositDTO.depositCoordinates());

        repository.save(savedVehicleDeposit);
    }
}
