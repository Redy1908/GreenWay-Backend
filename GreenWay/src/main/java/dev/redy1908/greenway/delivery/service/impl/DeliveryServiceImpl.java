package dev.redy1908.greenway.delivery.service.impl;

import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.LatLng;
import dev.redy1908.greenway.delivery.dto.DeliveryCreationDto;
import dev.redy1908.greenway.delivery.dto.DeliveryDto;
import dev.redy1908.greenway.delivery.dto.DeliveryPageResponseDTO;
import dev.redy1908.greenway.delivery.exceptions.DeliveryNotFoundException;
import dev.redy1908.greenway.delivery.mapper.DeliveryMapper;
import dev.redy1908.greenway.delivery.model.Delivery;
import dev.redy1908.greenway.delivery.repository.DeliveryRepository;
import dev.redy1908.greenway.delivery.service.IDeliveryService;
import dev.redy1908.greenway.deliveryMan.model.DeliveryMan;
import dev.redy1908.greenway.deliveryMan.service.impl.DeliveryManService;
import dev.redy1908.greenway.deliveryPath.model.DeliveryPath;
import dev.redy1908.greenway.deliveryPath.repository.DeliveryPathRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements IDeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;

    private final DeliveryManService deliveryManService;
    private final DeliveryPathRepository deliveryPathRepository;

    private final RestTemplate restTemplate;

    @Value("${osrm.base_path}")
    private String OSRM_BASE_PATH;

    @Override
    @Transactional
    public DeliveryDto createDelivery(DeliveryCreationDto deliveryCreationDto) {

        GeometryFactory geometryFactory = new GeometryFactory();

        Point start = geometryFactory.createPoint(new Coordinate(deliveryCreationDto.startPoint().latitude(), deliveryCreationDto.startPoint().longitude()));
        Point end = geometryFactory.createPoint(new Coordinate(deliveryCreationDto.endPoint().latitude(), deliveryCreationDto.endPoint().longitude()));

        String osrmResponse = getRouting(start, end);
        String encodedPolyline = extractEncodedPolyline(osrmResponse);
        LineString decodedPolyline = decodePolylineToLineString(encodedPolyline);
        Double distance = extractDistance(osrmResponse);
        Double duration = extractDuration(osrmResponse);

        DeliveryPath deliveryPath = new DeliveryPath(
                start,
                end,
                distance,
                duration,
                encodedPolyline
        );

        deliveryPathRepository.save(deliveryPath);
        DeliveryMan deliveryMan = deliveryManService.findByUsername(deliveryCreationDto.deliveryManUsername());

        Delivery delivery = new Delivery(
                deliveryMan,
                deliveryPath
        );

        Delivery savedDelivery = deliveryRepository.save(delivery);

        return deliveryMapper.toDto(savedDelivery);
    }

    @Override
    public DeliveryDto getDeliveryById(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(
                () -> new DeliveryNotFoundException(deliveryId)
        );

        return deliveryMapper.toDto(delivery);
    }

    @Override
    public DeliveryPageResponseDTO getDeliveriesByDeliveryMan(String deliveryManUsername, int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Delivery> deliveries = deliveryRepository.findAllByDeliveryMan_Username(deliveryManUsername, pageable);
        List<Delivery> listDeliveries = deliveries.getContent();
        List<DeliveryDto> content =  listDeliveries.stream().map(deliveryMapper::toDto).toList();

        System.out.println(listDeliveries);

        DeliveryPageResponseDTO deliveryPageResponseDTO = new DeliveryPageResponseDTO();
        deliveryPageResponseDTO.setContent(content);
        deliveryPageResponseDTO.setPageNo(deliveries.getNumber());
        deliveryPageResponseDTO.setPageSize(deliveries.getSize());
        deliveryPageResponseDTO.setTotalElements(deliveries.getTotalElements());
        deliveryPageResponseDTO.setTotalPages(deliveries.getTotalPages());
        deliveryPageResponseDTO.setLast(deliveries.isLast());

        return deliveryPageResponseDTO;
    }

    @Override
    public boolean isDeliveryOwner(Long deliveryId, String deliveryManUsername) {
        return deliveryRepository.getDeliveryByIdAndDeliveryMan_Username(deliveryId, deliveryManUsername).isPresent();
    }

    private String getRouting(Point start, Point end){
        String url = OSRM_BASE_PATH + "/route/v1/driving/" + start.getX() + "," + start.getY() + ";" + end.getX() + "," + end.getY();
        return restTemplate.getForEntity(url, String.class).getBody();
    }

    private String extractEncodedPolyline(String osrmResponse){
        JSONObject jsonObject = new JSONObject(osrmResponse);
        JSONObject route = jsonObject.getJSONArray("routes").getJSONObject(0);
        return route.getString("geometry");
    }

    private Double extractDistance(String osrmResponse){
        JSONObject jsonObject = new JSONObject(osrmResponse);
        JSONObject route = jsonObject.getJSONArray("routes").getJSONObject(0);
        return route.getDouble("distance");
    }

    private Double extractDuration(String osrmResponse){
        JSONObject jsonObject = new JSONObject(osrmResponse);
        JSONObject route = jsonObject.getJSONArray("routes").getJSONObject(0);
        return route.getDouble("duration");
    }

    private LineString decodePolylineToLineString(String encodedPolyline){
        List<LatLng> latLngs = PolylineEncoding.decode(encodedPolyline);

       Coordinate[] coordinates = latLngs.stream()
                .map(latlng -> new Coordinate(latlng.lng, latlng.lat))
                .toArray(Coordinate[]::new);

        return new GeometryFactory().createLineString(coordinates);
    }
}
