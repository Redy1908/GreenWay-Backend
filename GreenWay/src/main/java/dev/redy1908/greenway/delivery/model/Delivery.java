package dev.redy1908.greenway.delivery.model;

import dev.redy1908.greenway.app.common.model.BaseEntity;
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
public class Delivery extends BaseEntity {

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "delivery_man_id")
    private DeliveryMan deliveryMan;

    @OneToOne
    @JoinColumn(name = "delivery_path_id")
    private DeliveryPath deliveryPath;

    public Delivery(DeliveryMan deliveryMan, DeliveryPath deliveryPath) {
        this.deliveryMan = deliveryMan;
        this.deliveryPath = deliveryPath;
    }
}
