package dev.redy1908.greenway.delivery_path.domain;

import dev.redy1908.greenway.osrm.domain.IOsrmService;
import dev.redy1908.greenway.osrm.domain.OsrmParsedData;
import dev.redy1908.greenway.point.Point;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class DeliveryPathService implements IDeliveryPathService {

    private final IOsrmService osrmService;
    private final DeliveryPathRepository deliveryPathRepository;

    @Override
    public DeliveryPath createDeliveryPath(Point startPoint, List<Point> pointList) {
        OsrmParsedData osrmParsedData = osrmService.getParsedData(startPoint, pointList);

        Double distanceInMeters = osrmParsedData.distanceInMeters();
        Double durationInSeconds = osrmParsedData.durationInSeconds();
        String polyline = osrmParsedData.polyline();

        return deliveryPathRepository.save(new DeliveryPath(
                distanceInMeters,
                durationInSeconds,
                polyline));
    }
}
