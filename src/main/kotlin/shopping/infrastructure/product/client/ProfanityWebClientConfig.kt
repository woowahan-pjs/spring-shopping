package shopping.infrastructure.product.client

import io.netty.channel.ChannelOption
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
import reactor.netty.resources.LoopResources
import java.time.Duration

@Configuration
class ProfanityWebClientConfig {
    @Bean
    fun profanityWebClient(): WebClient {
        val connectionProvider =
            ConnectionProvider
                .builder("profanity")
                .maxConnections(10)
                .maxIdleTime(Duration.ofSeconds(20))
                .maxLifeTime(Duration.ofSeconds(60))
                .pendingAcquireTimeout(Duration.ofSeconds(5))
                .build()

        val loopResources =
            LoopResources.create(
                "profanity",
                1,
                1,
                false,
            )

        val httpClient =
            HttpClient
                .create(connectionProvider)
                .runOn(loopResources)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .responseTimeout(Duration.ofSeconds(3))

        return WebClient
            .builder()
            .baseUrl("https://www.purgomalum.com")
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .build()
    }
}
