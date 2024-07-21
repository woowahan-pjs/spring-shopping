package shopping.product.infra;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import shopping.product.exception.PurgoMalumRetrieveFailException;

import java.util.List;

@Component
public class ProfanityChecker {

    private static final String PURGOMALUM_URL = "https://www.purgomalum.com";
    private final RestClient restClient;
    private final List<String> profanities = List.of(
            "바보",
            "병신",
            "시발",
            "씨발",
            "개새끼",
            "지랄",
            "미친놈",
            "미친년"
    );

    public ProfanityChecker(final RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl(PURGOMALUM_URL)
                .build();
    }

    public boolean containsProfanity(final String name) {
        if (profanities.contains(name)) {
            return true;
        }

        return restClient.get()
                .uri("/service/containsprofanity?text={text}", name)
                .exchange((request, response) -> {
                    if (response.getStatusCode().is4xxClientError()) {
                        throw new PurgoMalumRetrieveFailException(response.getStatusText());
                    }
                    final String body = new String(response.getBody().readAllBytes());
                    return Boolean.parseBoolean(body);
                });
    }
}
