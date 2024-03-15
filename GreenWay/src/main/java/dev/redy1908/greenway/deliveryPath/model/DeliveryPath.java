package dev.redy1908.greenway.deliveryPath.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

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

    @Column(columnDefinition = "TEXT")
    private String encodedPolyline;

    @Column(columnDefinition = "geometry")
    private LineString polyline;

    public DeliveryPath(Point startPoint, Point endPoint, Double distance, Double duration, String encodedPolyline) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.distance = distance;
        this.duration = duration;
        this.encodedPolyline = encodedPolyline;
    }
}
