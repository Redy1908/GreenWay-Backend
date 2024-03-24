package dev.redy1908.greenway.delivery_path.domain;

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
@Table(name = "delivery_paths")
public class DeliveryPath {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double distanceInMeters;

    @Column(nullable = false)
    private Double durationInSeconds;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String polyline;

    public DeliveryPath(Double distanceInMeters, Double durationInSeconds, String polyline) {
        this.distanceInMeters = distanceInMeters;
        this.durationInSeconds = durationInSeconds;
        this.polyline = polyline;
    }
}
