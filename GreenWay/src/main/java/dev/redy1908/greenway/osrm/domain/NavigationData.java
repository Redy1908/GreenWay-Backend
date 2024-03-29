package dev.redy1908.greenway.osrm.domain;

import java.util.Map;

public record NavigationData(
        Map<String, Object> osrmResponse
) {
}
