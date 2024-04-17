package dev.redy1908.greenway.vehicle_deposit.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleDepositRepository extends JpaRepository<VehicleDeposit, Integer> {

}
