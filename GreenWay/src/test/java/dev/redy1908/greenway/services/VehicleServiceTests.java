package dev.redy1908.greenway.services;

import static org.assertj.core.api.Assertions.assertThat;

import dev.redy1908.greenway.vehicle.domain.Vehicle;
import dev.redy1908.greenway.vehicle.domain.VehicleMapper;
import dev.redy1908.greenway.vehicle.domain.VehicleRepository;
import dev.redy1908.greenway.vehicle.domain.dto.VehicleCreationDTO;
import dev.redy1908.greenway.vehicle.domain.dto.VehicleDTO;
import dev.redy1908.greenway.vehicle.domain.VehicleServiceImpl;
import dev.redy1908.greenway.vehicle.domain.exceptions.models.VehicleNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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

    private VehicleCreationDTO vehicleCreationDTO;

    private Vehicle vehicle;

    private VehicleDTO vehicleDTO;

    @BeforeEach
    void setup(){
        vehicleCreationDTO = new VehicleCreationDTO("Model1",100.0, 10.0, 50.0, 200.0);
        vehicle = new Vehicle("Model1", 100.0, 10.0, 50.0, 200.0);
        vehicleDTO = new VehicleDTO(1L, "Model1", 100.0, 10.0, 50.0, 200.0);
    }

    @Test
    void it_should_save_vehicle_returns_vehicle(){
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);

        Vehicle result = vehicleService.saveVehicle(vehicleCreationDTO);

        ArgumentCaptor<Vehicle> captor = ArgumentCaptor.forClass(Vehicle.class);
        verify(vehicleRepository).save(captor.capture());

        //Vehicle savedVehicle = captor.getValue();

        assertThat(result).isEqualTo(vehicle);
    }

    @Test
    void it_should_find_vehicle_when_valid_id_returns_vehicle(){
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(vehicleMapper.toDto(vehicle)).thenReturn(vehicleDTO);

        VehicleDTO result = vehicleService.getVehicleById(1L);

        verify(vehicleRepository).findById(1L);
        verify(vehicleMapper).toDto(vehicle);

        assertThat(result).isEqualTo(vehicleDTO);
    }

    @Test
    void it_should_throw_exception_when_find_vehicle_with_bad_id_throws_VehicleNotFound(){
        when(vehicleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(VehicleNotFoundException.class, () -> vehicleService.getVehicleById(1L));

        verify(vehicleRepository).findById(1L);
    }
}
