package dev.redy1908.greenway.trip.web;

import dev.redy1908.greenway.app.web.models.PageResponseDTO;
import dev.redy1908.greenway.trip.domain.DTO.TripHistoryDTO;
import dev.redy1908.greenway.trip.domain.ITripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/trip/histories")
public class TripController {

    private final ITripService tripService;

    @GetMapping
    public ResponseEntity<PageResponseDTO<TripHistoryDTO>> getAllTripHistories(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {

        PageResponseDTO<TripHistoryDTO> deliveryPageResponseDTO = tripService.findAll(pageNo, pageSize);

        return ResponseEntity.ok().body(deliveryPageResponseDTO);
    }

}
