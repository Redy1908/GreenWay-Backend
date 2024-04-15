package dev.redy1908.greenway.delivery_vehicle.domain;

import dev.redy1908.greenway.base_entity.domain.BaseEntity;
import dev.redy1908.greenway.delivery.domain.Delivery;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
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

    @Column(nullable = false)
    private String depositAddress;

    @Column(columnDefinition = "geography", nullable = false)
    private Point depositCoordinates;

    @OneToMany(mappedBy = "deliveryVehicle", orphanRemoval = true)
    private Set<Delivery> deliveries = new LinkedHashSet<>();

}
