package shopping.core;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;


@TestConfiguration
@ComponentScan("shopping")
public class TestConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public DatabaseCleaner databaseCleaner() {
        return new DatabaseCleaner(entityManager);
    }
}
