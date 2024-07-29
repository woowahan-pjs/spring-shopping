package shopping.config

import org.mockito.kotlin.mock
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import shopping.product.domain.BadWordValidator

@Profile("test")
@Configuration
class BadWordValidatorTestConfig {
    @Bean
    @Primary
    fun badWordValidator(): BadWordValidator = mock()
}
