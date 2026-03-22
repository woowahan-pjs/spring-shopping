package shopping.infrastructure.product.client

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import shopping.domain.product.ProductBadWordClient

@Component
class ProductBadWordClientAdapter(
    private val profanityWebClient: WebClient,
) : ProductBadWordClient {
    override fun containsProfanity(text: String): Boolean =
        profanityWebClient
            .get()
            .uri("/service/containsprofanity?text={text}", text)
            .retrieve()
            .bodyToMono(Boolean::class.java)
            .block() ?: false
}
