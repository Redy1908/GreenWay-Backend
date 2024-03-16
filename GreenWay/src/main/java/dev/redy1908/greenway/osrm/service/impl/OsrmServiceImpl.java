package dev.redy1908.greenway.osrm.service.impl;

import dev.redy1908.greenway.osrm.service.IosrmService;
import dev.redy1908.greenway.point.Point;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OsrmServiceImpl implements IosrmService {

    @Value("${osrm.driving_path}")
    private String OSRM_DRIVING_PATH;

    private final RestTemplate restTemplate;

    @Override
    public String getRouting(Point startPoint, List<Point> destinations) {

        System.out.println(destinations.toString());

        String url = OSRM_DRIVING_PATH + startPoint.longitude() + "," + startPoint.latitude() + ";";

        url += destinations.stream().map(
                point -> point.longitude() + "," + point.latitude())
                .collect(Collectors.joining(";"));

        System.out.println(url);

        return restTemplate.getForEntity(url, String.class).getBody();
    }
}
