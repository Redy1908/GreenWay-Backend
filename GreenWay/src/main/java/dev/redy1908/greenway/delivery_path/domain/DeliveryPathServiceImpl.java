package dev.redy1908.greenway.delivery_path.domain;

import java.util.List;

import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.redy1908.greenway.osrm.domain.IOsrmService;
import dev.redy1908.greenway.osrm.domain.OsrmParsedData;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
class DeliveryPathServiceImpl implements IDeliveryPathService {

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
