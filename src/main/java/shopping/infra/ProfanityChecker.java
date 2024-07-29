package shopping.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class ProfanityChecker {

    private final RestClient restClient;

    public boolean hasProfanity(String text) {
        final String response = restClient.get()
                .uri("https://www.purgomalum.com/service/containsprofanity?text={text}", text)
                .retrieve()
                .body(String.class);
        return Boolean.parseBoolean(response);
    }
}
