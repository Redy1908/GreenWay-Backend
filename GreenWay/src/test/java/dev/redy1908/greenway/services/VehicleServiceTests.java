package dev.redy1908.greenway.services;

import dev.redy1908.greenway.vehicle.domain.Vehicle;
import dev.redy1908.greenway.vehicle.domain.VehicleMapper;
import dev.redy1908.greenway.vehicle.domain.VehicleRepository;
import dev.redy1908.greenway.vehicle.domain.VehicleServiceImpl;
import dev.redy1908.greenway.vehicle.domain.dto.VehicleDTO;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTests {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private VehicleMapper vehicleMapper;

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    @Test
    void it_should_save_vehicle_returns_vehicle() {

        VehicleDTO vehicleDTO = new VehicleDTO(
                "Test Vehicle 1",
                100.0,
                10.0,
                50.0,
                200.0);

        Vehicle vehicle = new Vehicle("Test Vehicle 1",
                0.0,
                0.0,
                0.0,
                0.0);

        Vehicle savedVehicle = new Vehicle("Test Vehicle 1",
                0.0,
                0.0,
                0.0,
                0.0);

        savedVehicle.setId(1L);

        when(vehicleMapper.toEntity(vehicleDTO)).thenReturn(vehicle);
        when(vehicleRepository.save(vehicle)).thenReturn(savedVehicle);

        Vehicle result = vehicleService.saveVehicle(vehicleDTO);

        verify(vehicleMapper).toEntity(vehicleDTO);
        verify(vehicleRepository).save(vehicle);

        assertThat(result).isEqualTo(savedVehicle);
    }
}