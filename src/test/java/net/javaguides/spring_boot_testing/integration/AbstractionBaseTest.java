package net.javaguides.spring_boot_testing.integration;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

public abstract class AbstractionBaseTest {
    static final MySQLContainer MYSQL_CONTAINER;

    static {
        MYSQL_CONTAINER = new MySQLContainer("mysql:latest")
                .withUsername("root")
                .withPassword("password")
                .withDatabaseName("ems");
        MYSQL_CONTAINER.start();

    }

    @DynamicPropertySource
    public static void dynamicPropertySource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MYSQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.url", MYSQL_CONTAINER::getUsername);
        registry.add("spring.datasource.url", MYSQL_CONTAINER::getPassword);
    }
}
