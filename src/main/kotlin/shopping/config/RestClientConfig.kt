package shopping.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.JdkClientHttpRequestFactory
import org.springframework.web.client.RestClient
import java.net.http.HttpClient
import java.time.Duration

@Configuration
class RestClientConfig {
    @Bean("purgoMalumRestClient")
    fun purgoMalumRestClient(): RestClient {
        val requestFactory =
            JdkClientHttpRequestFactory(
                HttpClient
                    .newBuilder()
                    .connectTimeout(Duration.ofSeconds(3))
                    .build(),
            )
        requestFactory.setReadTimeout(Duration.ofSeconds(10))

        return RestClient
            .builder()
            .requestFactory(requestFactory)
            .baseUrl("https://www.purgomalum.com")
            .build()
    }
}
