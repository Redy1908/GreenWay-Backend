package dev.redy1908.greenway.delivery_man.domain;

import dev.redy1908.greenway.delivery_vehicle.domain.DeliveryVehicle;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "delivery_men")
public class DeliveryMan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "delivery_vehicle_id")
    private DeliveryVehicle deliveryVehicle;

    public DeliveryMan(String username) {
        this.username = username;
    }
}
