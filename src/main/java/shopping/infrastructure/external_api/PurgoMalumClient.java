package shopping.infrastructure.external_api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import shopping.application.profanity.ProfanityChecker;
import shopping.infrastructure.external_api.exception.ProfanityCheckException;

@Component
public class PurgoMalumClient implements ProfanityChecker {

    private final RestClient restClient;

    public PurgoMalumClient(
            RestClient.Builder restClientBuilder,
            @Value("${external.purgomalum.base-url}") String baseUrl
    ) {
        this.restClient = restClientBuilder
                .baseUrl(baseUrl)
                .defaultStatusHandler(HttpStatusCode::isError, (request, response) -> {
                    throw new ProfanityCheckException(
                            "Error occurred while checking for profanity. Status: " + response.getStatusCode()
                    );
                })
                .build();
    }

    @Override
    public boolean containsProfanity(String text) {
        if (text == null || text.isBlank()) {
            return false;
        }

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
