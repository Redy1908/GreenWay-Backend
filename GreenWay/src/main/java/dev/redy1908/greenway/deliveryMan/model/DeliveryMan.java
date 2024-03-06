package dev.redy1908.greenway.deliveryMan.model;

import dev.redy1908.greenway.vehicle.model.Vehicle;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class DeliveryMan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

}
