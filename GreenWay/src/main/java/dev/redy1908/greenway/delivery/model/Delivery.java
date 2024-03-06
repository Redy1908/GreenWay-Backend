package dev.redy1908.greenway.delivery.model;

import dev.redy1908.greenway.deliveryMan.model.DeliveryMan;
import dev.redy1908.greenway.deliveryPath.model.DeliveryPath;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "delivery_man")
    private DeliveryMan deliveryMan;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "delivery_path_id")
    private DeliveryPath deliveryPath;

    public Delivery(DeliveryMan deliveryMan, DeliveryPath deliveryPath) {
        this.deliveryMan = deliveryMan;
        this.deliveryPath = deliveryPath;
    }
}
