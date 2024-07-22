package shopping.product.infrastructure.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestClient;
import shopping.product.domain.ProfanityClient;

@Component
public class PurgoMalumClient implements ProfanityClient {
    public static final String PURGO_MALUM_API = "/service/containsprofanity?text={text}";
    private final RestClient builder;

    public PurgoMalumClient(@Value("${service.abusing.url}") final String baseUrl,
                            final RestClient.Builder builder) {
        this.builder = builder.baseUrl(baseUrl).build();
    }

    @Override
    public boolean containProfanity(final String word) {
        final String result = builder.get()
                .uri(PURGO_MALUM_API, word)
                .accept(MediaType.TEXT_PLAIN)
                .retrieve()
                .onStatus(new DefaultResponseErrorHandler())
                .body(String.class);
        return Boolean.valueOf(result);
    }
}
