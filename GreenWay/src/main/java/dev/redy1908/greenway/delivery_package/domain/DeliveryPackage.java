package dev.redy1908.greenway.delivery_package.domain;

import dev.redy1908.greenway.app.common.model.BaseEntity;
import dev.redy1908.greenway.delivery.domain.Delivery;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;
import org.locationtech.jts.geom.Point;

import java.util.Objects;

@Entity
@Getter
@Setter
public class DeliveryPackage extends BaseEntity {

    @Column(columnDefinition = "geography")
    private Point destination;

    private Double weight;

    @ManyToOne
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

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
        DeliveryPackage that = (DeliveryPackage) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }
}
