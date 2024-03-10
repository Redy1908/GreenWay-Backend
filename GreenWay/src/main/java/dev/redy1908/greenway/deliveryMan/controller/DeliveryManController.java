package dev.redy1908.greenway.deliveryMan.controller;

import dev.redy1908.greenway.deliveryMan.service.impl.DeliveryManService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/deliveryMen")
public class DeliveryManController {

    private final DeliveryManService deliveryManService;
}
