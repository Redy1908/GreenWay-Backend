package dev.redy1908.greenway.delivery_man.web;

import dev.redy1908.greenway.app.web.models.ResponseDTO;
import dev.redy1908.greenway.delivery_man.domain.DeliveryMan;
import dev.redy1908.greenway.delivery_man.domain.IDeliveryManService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/deliveryMen", produces = MediaType.APPLICATION_JSON_VALUE)
public class DeliveryManController {

    private final IDeliveryManService deliveryManService;

    @PostMapping
    public ResponseEntity<ResponseDTO> save(@CurrentSecurityContext(expression = "authentication.principal") Jwt jwt) {

        String username = jwt.getClaim("preferred_username");
        DeliveryMan savedDeliveryMan = deliveryManService.save(username);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/v1/deliveryMen/{deliveryManId}")
                .buildAndExpand(savedDeliveryMan.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(new ResponseDTO(HttpStatus.CREATED.value(), HttpStatus.CREATED, "Deliveryman created"));
    }
}
