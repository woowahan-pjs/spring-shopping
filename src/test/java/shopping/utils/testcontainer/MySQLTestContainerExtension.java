package shopping.utils.testcontainer;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.MySQLContainer;

public class MySQLTestContainerExtension implements Extension, BeforeAllCallback {

    private static final String DOCKER_IMAGE = "mysql:8.0.32";
    private static final MySQLContainer MYSQL_CONTAINER = new MySQLContainer<>(DOCKER_IMAGE)
            .withDatabaseName("shopping")
            .withUsername("root")
            .withPassword("1234");


    @Override
    public void beforeAll(final ExtensionContext extensionContext) {
        if (MYSQL_CONTAINER.isRunning()) {
            return;
        }
        MYSQL_CONTAINER.start();
        System.setProperty("spring.datasource.url", MYSQL_CONTAINER.getJdbcUrl());
        System.setProperty("spring.datasource.username", MYSQL_CONTAINER.getUsername());
        System.setProperty("spring.datasource.password", MYSQL_CONTAINER.getPassword());
        System.setProperty("spring.datasource.driver-class-name", MYSQL_CONTAINER.getDriverClassName());
    }
}
