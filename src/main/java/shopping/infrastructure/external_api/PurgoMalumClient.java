package shopping.infrastructure.external_api;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import shopping.domain.product.ProfanityChecker;

@Component
public class PurgoMalumClient implements ProfanityChecker {
    private final RestClient restClient;

    public PurgoMalumClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl("https://www.purgomalum.com/service")
                .build();
    }

    @Override
    public boolean containsProfanity(String text) {
        String result = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/containsprofanity")
                        .queryParam("text", text)
                        .build())
                .retrieve()
                .body(String.class);

        return Boolean.parseBoolean(result);
    }
}
