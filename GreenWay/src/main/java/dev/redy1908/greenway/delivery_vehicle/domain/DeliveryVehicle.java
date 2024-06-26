package dev.redy1908.greenway.delivery_vehicle.domain;

import dev.redy1908.greenway.base_entity.domain.BaseEntity;
import dev.redy1908.greenway.delivery.domain.Delivery;
import dev.redy1908.greenway.delivery_man.domain.DeliveryMan;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @OrderColumn
    @OneToMany(mappedBy = "deliveryVehicle", fetch = FetchType.EAGER)
    private List<Delivery> deliveries = new ArrayList<>();

    @OneToOne(mappedBy = "deliveryVehicle")
    private DeliveryMan deliveryMan;

}
