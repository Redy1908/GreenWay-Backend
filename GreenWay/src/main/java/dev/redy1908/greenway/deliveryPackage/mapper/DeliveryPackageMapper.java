package dev.redy1908.greenway.deliveryPackage.mapper;

import dev.redy1908.greenway.deliveryPackage.dto.DeliveryPackageDTO;
import dev.redy1908.greenway.deliveryPackage.model.DeliveryPackage;
import dev.redy1908.greenway.point.Point;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface DeliveryPackageMapper {

    @Mapping(target = "destination", source = "destination", qualifiedByName = "mapToGreenWayPoint")
    DeliveryPackageDTO toDto(DeliveryPackage deliveryPackage);

    @Mapping(target = "destination", source = "destination", qualifiedByName = "mapToGeometryPoint")
    DeliveryPackage toEntity(DeliveryPackageDTO deliveryPackageDTO);

    @Named("mapToGeometryPoint")
    default org.locationtech.jts.geom.Point mapToGeometryPoint(Point point) {
        GeometryFactory geometryFactory = new GeometryFactory();
        return geometryFactory.createPoint(new Coordinate(point.latitude(), point.longitude()));
    }

    @Named("mapToGreenWayPoint")
    default Point mapToGreenWayPoint(org.locationtech.jts.geom.Point point) {
        return new Point(point.getX(), point.getY());
    }
}
