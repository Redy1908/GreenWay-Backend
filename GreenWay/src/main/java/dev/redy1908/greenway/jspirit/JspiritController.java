package dev.redy1908.greenway.jspirit;

import dev.redy1908.greenway.delivery.domain.DeliveryRepository;
import dev.redy1908.greenway.delivery_vehicle.domain.DeliveryVehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/jspirit")
@RequiredArgsConstructor
public class JspiritController {

    private final JspritService jspritService;
    private final DeliveryVehicleRepository deliveryVehicleRepository;
    private final DeliveryRepository deliveryRepository;

    @GetMapping
    public void test(){
        jspritService.test(deliveryVehicleRepository.findAll(), deliveryRepository.findAll());
    }
}
