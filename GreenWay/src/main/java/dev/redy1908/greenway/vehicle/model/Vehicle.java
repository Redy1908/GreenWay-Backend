package dev.redy1908.greenway.vehicle.model;

import dev.redy1908.greenway.deliveryMan.model.DeliveryMan;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
/**
 * @param batteryNominalCapacity battery capacity in kWh
 * @param vehicleConsumption energy consumed per km Wh/km
 * @param chargePortType the type of the charging port fot compatibility check
 * @param chargingPower the power of charging kW (AT THE MOMENT FAST CHARGING IS NOT TAKE INTO ACCOUNT)
 */
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, updatable = false)
    private String model;

    private Double batteryNominalCapacity;
    private Double vehicleConsumption;
    private String chargePortType;
    private Double chargingPower;
    private Double currentBatteryCharge;
    private Boolean isFree;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "delivery_man_id")
    private DeliveryMan deliveryMan;

}
