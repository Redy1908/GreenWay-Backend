package dev.redy1908.greenway.trip.domain;

import dev.redy1908.greenway.app.web.models.PageResponseDTO;
import dev.redy1908.greenway.trip.domain.DTO.TripHistoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TripServiceImpl implements ITripService {

    private final TripRepository tripRepository;
    private final TripMapper tripMapper;

    @Override
    public Trip save(Trip trip) {
        return tripRepository.save(trip);
    }


    @Override
    public PageResponseDTO<TripHistoryDTO> findAll(int pageNo, int pageSize) {
        Page<Trip> elements = tripRepository.findAll(PageRequest.of(pageNo, pageSize));
        List<Trip> listElements = elements.getContent();
        List<TripHistoryDTO> content = listElements.stream().map(tripMapper::tripToTripHistoryDTO).toList();

        PageResponseDTO<TripHistoryDTO> pageResponseDTO = new PageResponseDTO<>();
        pageResponseDTO.setContent(content);
        pageResponseDTO.setPageNo(elements.getNumber());
        pageResponseDTO.setPageSize(elements.getSize());
        pageResponseDTO.setTotalElements(elements.getTotalElements());
        pageResponseDTO.setTotalPages(elements.getTotalPages());
        pageResponseDTO.setLast(elements.isLast());

        return pageResponseDTO;
    }
}
