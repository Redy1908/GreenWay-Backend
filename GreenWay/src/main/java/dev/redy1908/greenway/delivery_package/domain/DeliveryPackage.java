package dev.redy1908.greenway.delivery_package.domain;

import dev.redy1908.greenway.base_entity.domain.BaseEntity;
import dev.redy1908.greenway.delivery.domain.Delivery;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

@Entity
@Getter
@Setter
@Table(name = "delivery_packages")
public class DeliveryPackage extends BaseEntity {

    @Column(nullable = false)
    private String sender;

    @Column(nullable = false)
    private String senderAddress;

    @Column(nullable = false)
    private String receiver;

    @Column(nullable = false)
    private String receiverAddress;

    @Column(columnDefinition = "geography")
    private Point receiverCoordinates;

    @Column(nullable = false)
    private Double weight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;
}
