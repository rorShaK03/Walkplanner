package testContainer;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class IntegrationEnvironment {

    protected static PostgreSQLContainer<?> container = new PostgreSQLContainer<>(DockerImageName.parse("postgres:14")
            .asCompatibleSubstituteFor("postgres"))
            .withDatabaseName("databaseName")
            .withUsername("username")
            .withPassword("password");

    static {
        container.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);

        registry.add("app.generate-synthetic-data", () -> "false");
    }
}
