package dev.redy1908.greenway.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import dev.redy1908.greenway.util.BaseTest;
import dev.redy1908.greenway.vehicle.domain.Vehicle;
import dev.redy1908.greenway.vehicle.domain.VehicleRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class VehicleRepositoryTests extends BaseTest {

    @Autowired
    private VehicleRepository vehicleRepository;

    private Vehicle vehicle;

    private List<Vehicle> vehicleList;

    @BeforeEach
    public void setup() {

        vehicle = new Vehicle(
                "Test Vehicle 1",
                0.0,
                0.0,
                0.0,
                0.0);

        vehicleList = new ArrayList<>(Arrays.asList(
                new Vehicle(
                        "Test Vehicle 2",
                        0.0,
                        0.0,
                        0.0,
                        0.0
                ),
                new Vehicle(
                        "Test Vehicle 3",
                        0.0,
                        0.0,
                        0.0,
                        0.0
                )
        ));
    }

    @Test
    void it_should_save_vehicle() {
        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        assertThat(savedVehicle).isNotNull();
        assertThat(savedVehicle.getId()).isNotNull();
        assertThat(savedVehicle.getId()).isPositive();
        assertThat(savedVehicle.getCreatedBy()).isEqualTo(auditor);
    }

    @Test
    void it_should_save_all_vehicles() {
        List<Vehicle> savedVehicles = vehicleRepository.saveAll(vehicleList);

        assertThat(savedVehicles)
                .isNotNull()
                .hasSize(2)
                .satisfies(vehicles -> {
                    assertThat(vehicles.getFirst().getId()).isPositive();
                    assertThat(vehicles.getFirst().getCreatedBy()).isEqualTo(auditor);
                    assertThat(vehicles.get(1).getId()).isPositive();
                    assertThat(vehicles.get(1).getCreatedBy()).isEqualTo(auditor);
                });
    }

    @Test
    void it_should_find_vehicle_when_valid_id(){
        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        Optional<Vehicle> vehicle = vehicleRepository.findById(savedVehicle.getId());

        assertThat(vehicle).isPresent();
    }

    @Test
    void it_should_find_all_vehicles(){

        vehicleRepository.saveAll(vehicleList);

        List<Vehicle> vehicleList = vehicleRepository.findAll();

        assertThat(vehicleList).isNotNull().hasSize(2);
    }

}
