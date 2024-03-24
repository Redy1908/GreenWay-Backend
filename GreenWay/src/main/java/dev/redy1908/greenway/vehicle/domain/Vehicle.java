package dev.redy1908.greenway.vehicle.domain;

import dev.redy1908.greenway.base_entity.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
/**
 * @param batteryNominalCapacity battery capacity in kWh
 * @param vehicleConsumption     energy consumed per km Wh/km
 */
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle extends BaseEntity {

    private String model;
    private Double batteryNominalCapacity;
    private Double vehicleConsumption;
    private Double currentBatteryCharge;
    private Double maxCapacity;

}
