package dev.redy1908.greenway.point.mapper;

import dev.redy1908.greenway.point.Point;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface PointMapper {

    @Mapping(source = "x", target = "latitude")
    @Mapping(source = "y", target = "longitude")
    Point toGreenWayPoint(org.locationtech.jts.geom.Point geometryPoint);


    default org.locationtech.jts.geom.Point toGeometryPoint(Point point){
        GeometryFactory geometryFactory = new GeometryFactory();
        return geometryFactory.createPoint(new Coordinate(point.latitude(), point.longitude()));
    }
}
