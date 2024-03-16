package dev.redy1908.greenway.delivery.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import dev.redy1908.greenway.app.common.model.BaseEntity;
import dev.redy1908.greenway.deliveryMan.model.DeliveryMan;
import dev.redy1908.greenway.deliveryPackage.model.DeliveryPackage;
import dev.redy1908.greenway.deliveryPath.model.DeliveryPath;
import dev.redy1908.greenway.vehicle.model.Vehicle;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Delivery extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "delivery_path_id")
    private DeliveryPath deliveryPath;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @JsonManagedReference
    @OneToOne(orphanRemoval = false)
    @JoinColumn(name = "delivery_man_id")
    private DeliveryMan deliveryMan;

    @OneToMany(mappedBy = "delivery")
    private List<DeliveryPackage> deliveryPackages = new ArrayList<>();

    public Delivery(DeliveryPath deliveryPath, Vehicle vehicle) {
        this.deliveryPath = deliveryPath;
        this.vehicle = vehicle;
    }

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
