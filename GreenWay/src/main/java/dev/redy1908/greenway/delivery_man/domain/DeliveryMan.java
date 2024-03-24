package dev.redy1908.greenway.delivery_man.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import dev.redy1908.greenway.delivery.domain.Delivery;
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

    @JsonBackReference
    @OneToOne(mappedBy = "deliveryMan")
    private Delivery delivery;

    public DeliveryMan(String username) {
        this.username = username;
    }
}
