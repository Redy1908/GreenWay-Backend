package dev.redy1908.greenway.trip.domain;

import dev.redy1908.greenway.app.web.models.PageResponseDTO;
import dev.redy1908.greenway.trip.domain.DTO.TripHistoryDTO;

public interface ITripService {

    Trip save(Trip trip);


    PageResponseDTO<TripHistoryDTO> findAll(int pageNo, int pageSize);
}
