package dev.redy1908.greenway.security.config;

import dev.redy1908.greenway.security.DeliveryManFilter;
import dev.redy1908.greenway.security.KeyCloakRoleConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final DeliveryManFilter deliveryManFilter;

    private static final String ADMIN_ROLE = "GREEN_WAY_ADMIN";
    private static final String DELIVERY_MAN_ROLE = "GREEN_WAY_DELIVERY_MAN";

    private static final String[] POST_ADMIN_LIST_URL = {
            "/api/v1/vehicles",
            "/api/v1/deliveries"
    };

    private static final String[] GET_ADMIN_LIST_URL = {
            "/api/v1/vehicles",
            "/api/v1/deliveries"
    };

    private static final String[] GET_ADMIN_DELIVERY_MAN_LIST_URL = {
            "/api/v1/vehicles/?",
            "/api/v1/deliveries/id/?",
            "/api/v1/deliveries/?"
    };

    private static final String[] GET_DELIVERY_MAN_LIST_URL = {
            "/api/v1/deliveryMen",
    };

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, POST_ADMIN_LIST_URL).hasRole(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.GET, GET_ADMIN_LIST_URL).hasRole(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.GET, GET_ADMIN_DELIVERY_MAN_LIST_URL).hasAnyRole(ADMIN_ROLE, DELIVERY_MAN_ROLE)
                        .requestMatchers(HttpMethod.GET, GET_DELIVERY_MAN_LIST_URL).hasRole(DELIVERY_MAN_ROLE)
                        .anyRequest().authenticated())
                .addFilterAfter(deliveryManFilter, BasicAuthenticationFilter.class)
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer
                        .jwtAuthenticationConverter(jwtAuthenticationConverter())));

        return http.build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeyCloakRoleConverter());

        return jwtAuthenticationConverter;
    }

}
