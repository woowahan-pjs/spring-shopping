package shopping.product.infra;

import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ProfanityCheckClient {
    private static final Logger log = LoggerFactory.getLogger(ProfanityCheckClient.class);

    private final RestClient restClient;

    public ProfanityCheckClient(@Value("${profanity-check.url}") String profanityCheckUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(profanityCheckUrl)
                .build();
    }

    public boolean check(String value) {
        String responseBody = restClient.get()
                .uri(uriBuilder -> uriBuilder.path("/containsprofanity")
                        .queryParam("text", value)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError,
                        ((request, response) -> log.error("[Purgomalum] Internal Server Error")))
                .body(String.class);

        if (Objects.isNull(responseBody)) {
            log.warn("[Purgomalum] body 응답이 null입니다.");
        }

        return Boolean.parseBoolean(responseBody);
    }
}
