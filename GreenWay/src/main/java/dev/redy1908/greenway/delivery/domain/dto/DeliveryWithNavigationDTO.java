package dev.redy1908.greenway.delivery.domain.dto;

import dev.redy1908.greenway.osrm.domain.NavigationData;

public record DeliveryWithNavigationDTO(
        DeliveryDTO delivery,

        NavigationData navigationData
) {
}
