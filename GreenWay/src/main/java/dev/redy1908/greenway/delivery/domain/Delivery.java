package dev.redy1908.greenway.delivery.domain;

import dev.redy1908.greenway.base_entity.domain.BaseEntity;
import dev.redy1908.greenway.delivery_vehicle.domain.DeliveryVehicle;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;
import org.locationtech.jts.geom.Point;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@Table(name = "deliveries")
public class Delivery extends BaseEntity {

    @Column(nullable = false)
    private String depositAddress;

    @Column(nullable = false)
    private Point depositCoordinates;

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

    private LocalDate estimatedDeliveryDate;

    @Column(nullable = false)
    private int weightKg;

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
