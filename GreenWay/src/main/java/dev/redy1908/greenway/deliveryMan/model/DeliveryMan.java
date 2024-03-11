package dev.redy1908.greenway.deliveryMan.model;

import dev.redy1908.greenway.delivery.model.Delivery;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryMan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @OneToMany(mappedBy = "deliveryMan", orphanRemoval = true)
    private Set<Delivery> deliveries = new LinkedHashSet<>();

    public DeliveryMan(String username) {
        this.username = username;
    }
}
