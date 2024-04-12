package dev.redy1908.greenway.delivery.domain;

import dev.redy1908.greenway.base_entity.domain.BaseEntity;
import dev.redy1908.greenway.delivery_man.domain.DeliveryMan;
import dev.redy1908.greenway.delivery_package.domain.DeliveryPackage;
import dev.redy1908.greenway.vehicle.domain.Vehicle;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "deliveries")
public class Delivery extends BaseEntity {

    @Column(nullable = false)
    private String depositAddress;

    @Column(nullable = false)
    private Point depositCoordinates;

    @Column(nullable = false)
    private LocalDate estimatedDeliveryDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private DeliveryMan deliveryMan;

    @ManyToOne(fetch = FetchType.LAZY)
    private Vehicle vehicle;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "delivery")
    private Set<DeliveryPackage> deliveryPackages = new LinkedHashSet<>();
}
