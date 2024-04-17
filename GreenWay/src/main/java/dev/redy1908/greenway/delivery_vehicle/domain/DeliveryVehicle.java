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
@Table(name = "vehicles")
public class DeliveryVehicle extends BaseEntity {

    @Column(nullable = false)
    private String modelName;

    @Column(nullable = false)
    private int maxAutonomyKm;

    @Column(nullable = false)
    private int maxCapacityKg;

    @OrderColumn
    @OneToMany(mappedBy = "deliveryVehicle", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Delivery> deliveries = new ArrayList<>();

    @OneToOne(mappedBy = "deliveryVehicle", orphanRemoval = true)
    private DeliveryMan deliveryMan;

}
