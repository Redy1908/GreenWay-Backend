package dev.redy1908.greenway.security.config;

import dev.redy1908.greenway.security.KeyCloakRoleConverter;
import dev.redy1908.greenway.security.filter.DeliveryManFilter;
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

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests((auth) -> auth
                        .requestMatchers(HttpMethod.POST,"/api/v1/vehicles/**").hasRole("GREEN_WAY_ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/v1/vehicles/**").hasAnyRole("GREEN_WAY_ADMIN", "GREEN_WAY_DELIVERY_MAN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/deliveries/**").hasRole("GREEN_WAY_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/deliveries/**").hasAnyRole("GREEN_WAY_ADMIN", "GREEN_WAY_DELIVERY_MAN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/deliveries/**").hasAnyRole("GREEN_WAY_ADMIN", "GREEN_WAY_DELIVERY_MAN")
                        .anyRequest().authenticated()
                )
                .addFilterAfter(deliveryManFilter, BasicAuthenticationFilter.class)
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter())));

        return http.build();
    }

    public JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtAuthenticationConverter jwtAuthenticationConverter =
                new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter
                (new KeyCloakRoleConverter());

        return jwtAuthenticationConverter;
    }

}
