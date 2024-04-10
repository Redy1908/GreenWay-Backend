package dev.redy1908.greenway.vehicle.domain;

import dev.redy1908.greenway.base_entity.domain.BaseEntity;
import dev.redy1908.greenway.delivery.domain.Delivery;
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
@Table(name = "vehicles")
public class Vehicle extends BaseEntity {

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private Double batteryNominalCapacity;

    @Column(nullable = false)
    private Double vehicleConsumption;

    @Column(nullable = false)
    private Double currentBatteryCharge;

    @Column(nullable = false)
    private Double maxCapacity;

    @OneToOne(mappedBy = "vehicle", orphanRemoval = true)
    private Delivery delivery;

}
