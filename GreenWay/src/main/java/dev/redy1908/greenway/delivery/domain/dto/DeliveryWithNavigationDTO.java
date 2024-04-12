package dev.redy1908.greenway.delivery.domain.dto;

import java.util.Map;

public record DeliveryWithNavigationDTO(
        DeliveryDTO delivery,

        Map<String, Object> navigationData
) {
}
