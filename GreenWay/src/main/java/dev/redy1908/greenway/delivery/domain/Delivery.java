package dev.redy1908.greenway.delivery.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import dev.redy1908.greenway.base_entity.domain.BaseEntity;
import dev.redy1908.greenway.delivery_man.domain.DeliveryMan;
import dev.redy1908.greenway.delivery_package.domain.DeliveryPackage;
import dev.redy1908.greenway.vehicle.domain.Vehicle;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;
import org.locationtech.jts.geom.Point;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "deliveries")
public class Delivery extends BaseEntity {

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @JsonManagedReference
    @OneToOne(optional = false)
    @JoinColumn(name = "delivery_man_id", nullable = false)
    private DeliveryMan deliveryMan;

    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL)
    private Set<DeliveryPackage> deliveryPackages = new LinkedHashSet<>();

    @Column(nullable = false)
    private String depositAddress;

    @Column(nullable = false)
    private Point depositCoordinates;

    @Override
    public final boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy
                ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
                : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
                : this.getClass();
        if (thisEffectiveClass != oEffectiveClass)
            return false;
        Delivery delivery = (Delivery) o;
        return getId() != null && Objects.equals(getId(), delivery.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }
}
