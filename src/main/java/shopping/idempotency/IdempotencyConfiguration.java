package shopping.idempotency;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdempotencyConfiguration {

    @Bean
    public IdempotencyKeyRepository idempotencyKeyRepository() {
        return new InMemoryIdempotencyKeyRepository();
    }
}
