package dev.redy1908.greenway.trip.domain;


import dev.redy1908.greenway.trip.domain.DTO.TripHistoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TripMapper {

    TripHistoryDTO tripToTripHistoryDTO(Trip trip);

}
