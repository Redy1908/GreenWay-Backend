package dev.redy1908.greenway.delivery_vehicle.domain;

import dev.redy1908.greenway.base_entity.domain.BaseEntity;
import dev.redy1908.greenway.delivery_man.domain.DeliveryMan;
import dev.redy1908.greenway.trip.Trip;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "delivery_vehicles")
public class DeliveryVehicle extends BaseEntity {

    @Column(nullable = false)
    private String modelName;

    @Column(nullable = false)
    private int maxAutonomyKm;

    @Column(nullable = false)
    private int maxCapacityKg;

    @Column(nullable = false)
    private int currentLoadKg = 0;

    @OneToOne(mappedBy = "deliveryVehicle")
    private DeliveryMan deliveryMan;

    @OneToOne(mappedBy = "deliveryVehicle", fetch = FetchType.EAGER)
    private Trip trip;

}
