package dev.redy1908.greenway.deliveryPath.model;

import dev.redy1908.greenway.chargingStations.model.ChargingStation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.vividsolutions.jts.geom.Point;


import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryPath {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "geography")
    private Point startPoint;

    @Column(columnDefinition = "geography")
    private Point endPoint;

    private Double distance;
    private Double duration;
    private String encodedPolyline;

    @ManyToMany
    @JoinTable(name = "deliveryPath_chargingStations",
            joinColumns = @JoinColumn(name = "deliveryPath_id"),
            inverseJoinColumns = @JoinColumn(name = "chargingStation_id"))
    private Set<ChargingStation> chargingStations = new LinkedHashSet<>();

    public DeliveryPath(Point startPoint, Point endPoint, Double distance, Double duration, String encodedPolyline) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.distance = distance;
        this.duration = duration;
        this.encodedPolyline = encodedPolyline;
    }
}
