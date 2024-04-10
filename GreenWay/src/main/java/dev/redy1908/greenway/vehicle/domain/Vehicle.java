package dev.redy1908.greenway.vehicle.domain;

import dev.redy1908.greenway.base_entity.domain.BaseEntity;
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
@Table(name = "vehicles")
public class Vehicle extends BaseEntity {

    @Column(nullable = false)
    private String modelName;

    @Column(nullable = false)
    private Double maxAutonomyKm;

    @Column(nullable = false)
    private Double maxCapacityKg;

}
