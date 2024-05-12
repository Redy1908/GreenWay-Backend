package dev.redy1908.greenway.delivery.domain;

import dev.redy1908.greenway.delivery_vehicle.domain.DeliveryVehicle;
import dev.redy1908.greenway.base_entity.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "deliveries")
public class Delivery extends BaseEntity {

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

    @Column
    private LocalDateTime estimatedDeliveryTime;

    @Column(nullable = false)
    private int weightKg;

    @Column(nullable = false)
    private boolean inTransit = false;

    @Column
    private LocalDateTime deliveryTime;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private DeliveryVehicle deliveryVehicle;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Delivery delivery = (Delivery) o;
        return getId() != null && Objects.equals(getId(), delivery.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
