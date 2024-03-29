package dev.redy1908.greenway.delivery_man.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/deliveryMen", produces = MediaType.APPLICATION_JSON_VALUE)
public class DeliveryManController {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public void create() {
        /*
         * Convenience method to enable the creation of a DeliveryMan via Spring Security using the JWT token
         * see security/DeliveryManFilter.java
         */
    }
}
