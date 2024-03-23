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
public class DeliveryPath {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double distanceInMeters;
    private Double durationInSeconds;

    @Column(columnDefinition = "TEXT")
    private String polyline;

    public DeliveryPath(Double distanceInMeters, Double durationInSeconds, String polyline) {
        this.distanceInMeters = distanceInMeters;
        this.durationInSeconds = durationInSeconds;
        this.polyline = polyline;
    }
}