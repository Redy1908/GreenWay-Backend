package dev.redy1908.greenway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
public class GreenWayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GreenWayApplication.class, args);
    }

}
