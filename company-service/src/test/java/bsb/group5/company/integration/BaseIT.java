package bsb.group5.company.integration;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class BaseIT {

    @Container
    private static PostgreSQLContainer container = new PostgreSQLContainer(
            "postgres:13.3"
    )
            .withUsername("login")
            .withPassword("pass")
            .withDatabaseName("tests");

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }
}
