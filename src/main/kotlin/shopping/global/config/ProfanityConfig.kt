package shopping.global.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory
import shopping.product.infra.ProfanityRepository

@Configuration
class ProfanityConfig {
    @Bean
    fun profanityRepository(): ProfanityRepository {
        val restClient = RestClient.builder()
            .baseUrl(PURGO_MALUM_URL)
            .build()
        val factory = HttpServiceProxyFactory
            .builderFor(RestClientAdapter.create(restClient))
            .build()

        return factory.createClient(ProfanityRepository::class.java)
    }

    companion object {
        private const val PURGO_MALUM_URL = "https://www.purgomalum.com/"
    }
}
