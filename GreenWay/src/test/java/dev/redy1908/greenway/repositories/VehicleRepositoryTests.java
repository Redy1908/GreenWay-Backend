package dev.redy1908.greenway.repositories;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;

import dev.redy1908.greenway.util.BaseTest;
import dev.redy1908.greenway.vehicle.domain.Vehicle;
import dev.redy1908.greenway.vehicle.domain.VehicleRepository;

public class VehicleRepositoryTests extends BaseTest {

    @Autowired
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private Vehicle vehicle;

    @BeforeEach
    public void setup() {

        vehicle = new Vehicle(
                "Fiat e-Ducato",
                0.0,
                0.0,
                0.0,
                0.0);
    }

    @Test
    public void it_should_save_vehicle() {
        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        assertThat(savedVehicle).isNotNull();
        assertThat(savedVehicle.getId()).isNotNull();
        assertThat(savedVehicle.getId()).isGreaterThan(0);
        assertThat(savedVehicle.getCreatedBy()).isEqualTo(auditor);
    }

}
