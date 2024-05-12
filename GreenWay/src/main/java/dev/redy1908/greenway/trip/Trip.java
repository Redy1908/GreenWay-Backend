package dev.redy1908.greenway.trip;

import dev.redy1908.greenway.base_entity.domain.BaseEntity;
import dev.redy1908.greenway.delivery.domain.Delivery;
import dev.redy1908.greenway.delivery_vehicle.domain.DeliveryVehicle;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "trips")
public class Trip extends BaseEntity {

    @Column(columnDefinition = "TEXT", nullable = false)
    private String polylineStandard;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String polylineElevation;

    @Column(columnDefinition = "TEXT")
    private String elevations;

    @OneToOne
    @JoinColumn(name = "delivery_vehicle_id")
    private DeliveryVehicle deliveryVehicle;

    @OrderColumn
    @OneToMany(mappedBy = "trip")
    private List<Delivery> deliveries = new ArrayList<>();

}
