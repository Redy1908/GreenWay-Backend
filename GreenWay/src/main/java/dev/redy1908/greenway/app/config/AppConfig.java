package dev.redy1908.greenway.app.config;

import lombok.RequiredArgsConstructor;
import org.n52.jackson.datatype.jts.JtsModule;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;

@Configuration
@RequiredArgsConstructor
@EnableTransactionManagement
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
public class AppConfig {

    private final ObjectMapper objectMapper;

    @Bean
    RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

    @PostConstruct
    public void objectMapperSetUp() {
        objectMapper.registerModule(new JtsModule());
    }
}
