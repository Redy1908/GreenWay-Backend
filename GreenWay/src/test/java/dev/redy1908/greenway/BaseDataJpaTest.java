package dev.redy1908.greenway;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Provides a Postgis database container shared between all the tests that extends this class.
 * Avoiding creating a new container for each test class.
 * Docker must be running.
 */
public abstract class BaseDataJpaTest {

    public static final PostgreSQLContainer<?> postgis;
    static final DockerImageName image = DockerImageName.parse("postgis/postgis")
            .asCompatibleSubstituteFor("postgres");

    static {
        postgis = new PostgreSQLContainer<>(image);
        postgis.start();
    }
}