package dev.redy1908.greenway.deliveryPath.service.impl;

import dev.redy1908.greenway.deliveryPath.model.DeliveryPath;
import dev.redy1908.greenway.deliveryPath.repository.DeliveryPathRepository;
import dev.redy1908.greenway.deliveryPath.service.IDeliveryPathService;
import dev.redy1908.greenway.osrm.service.IosrmService;
import dev.redy1908.greenway.point.Point;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryPathService implements IDeliveryPathService {

    private final IosrmService osrmService;
    private final DeliveryPathRepository deliveryPathRepository;

    @Override
    public DeliveryPath createDeliveryPath(Point startPoint, List<Point> pointList) {
        String osmrResponse = osrmService.getRouting(startPoint, pointList);
        Double distanceInMeters = osrmService.extractDistance(osmrResponse);
        Double durationInSeconds = osrmService.extractDuration(osmrResponse);
        String polyline = osrmService.extractPolyline(osmrResponse);

        return deliveryPathRepository.save(new DeliveryPath(
                distanceInMeters,
                durationInSeconds,
                polyline
        ));
    }
}
