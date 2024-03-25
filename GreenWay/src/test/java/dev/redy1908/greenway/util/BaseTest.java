package dev.redy1908.greenway.util;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import dev.redy1908.greenway.app.audit.AuditAwareImpl;

@DataJpaTest
@Testcontainers
@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BaseTest {

    private static final DockerImageName image = DockerImageName.parse("postgis/postgis")
            .asCompatibleSubstituteFor("postgres");

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(image);

    @MockBean(name = "auditAwareImpl")
    private AuditAwareImpl auditAwareImpl;

    protected final String auditor = "ADMIN";

    @BeforeEach
    public void baseSetup() {
        when(auditAwareImpl.getCurrentAuditor()).thenReturn(Optional.of(auditor));
    }

    @Test
    void is_db_connected() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }
}
