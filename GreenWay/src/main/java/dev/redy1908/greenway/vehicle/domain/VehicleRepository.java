package dev.redy1908.greenway.vehicle.domain;

import dev.redy1908.greenway.vehicle.domain.dto.VehicleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    @Query("SELECT new dev.redy1908.greenway.vehicle.domain.dto.VehicleDTO(v.modelName, v.maxAutonomyKm, v.maxCapacityKg) FROM Vehicle v WHERE v.id = :vehicleId")
    Optional<VehicleDTO> findDTOById(@Param("vehicleId") Long vehicleId);

    Page<VehicleDTO> findAllBy(Pageable pageable);

    @Query("SELECT new dev.redy1908.greenway.vehicle.domain.dto.VehicleDTO(v.modelName, v.maxAutonomyKm, v.maxCapacityKg) FROM Vehicle v LEFT JOIN Delivery del ON v.id = del.vehicle.id WHERE del IS NULL")
    Page<VehicleDTO> findAllFreeVehicles(Pageable pageable);

    @Query("SELECT v from Vehicle v LEFT JOIN Delivery d ON v.id = d.vehicle.id WHERE d IS NULL AND v.id = :vehicleId")
    Optional<Vehicle> getVehicleIfFree(@Param("vehicleId") Long vehicleId);
}
