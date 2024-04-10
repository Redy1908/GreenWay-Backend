package dev.redy1908.greenway.delivery_man.web;

import dev.redy1908.greenway.app.web.models.PageResponseDTO;
import dev.redy1908.greenway.delivery_man.domain.IDeliveryManService;
import dev.redy1908.greenway.delivery_man.domain.dto.DeliveryManDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/deliveryMen", produces = MediaType.APPLICATION_JSON_VALUE)
public class DeliveryManController {

    private final IDeliveryManService deliveryManService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create() {
        /*
         * Convenience method to enable the creation of a DeliveryMan via Spring Security using the JWT token
         * see security/DeliveryManFilter.java
         */
    }

    @GetMapping
    public ResponseEntity<PageResponseDTO<DeliveryManDTO>> getAllFreeDeliveryMan(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {

        PageResponseDTO<DeliveryManDTO> deliveryManDTO = deliveryManService.findAllFreeDeliveryMan(pageNo, pageSize);

        return ResponseEntity.ok().body(deliveryManDTO);
    }
}
