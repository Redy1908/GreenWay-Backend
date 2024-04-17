package dev.redy1908.greenway.vehicle_deposit.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

@Entity
@Getter
@Setter
@Table(name = "vehicle_deposit")
public class VehicleDeposit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String depositAddress;

    @Column(nullable = false)
    private Point depositCoordinates;
}
