package shopping.product;

import shopping.product.domain.ProfanityChecker;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class PurgoMalumProfanityChecker implements ProfanityChecker {

    private final RestClient restClient;

    public PurgoMalumProfanityChecker(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("https://www.purgomalum.com").build();
    }

    @Override
    public boolean containsProfanity(String text) {
        String result = restClient.get().uri("/service/containsprofanity?text={text}", text)
                .retrieve().body(String.class);
        return "true".equals(result);
    }
}
