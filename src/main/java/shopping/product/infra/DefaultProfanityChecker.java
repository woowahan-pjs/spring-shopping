package shopping.product.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import shopping.product.domain.ProfanityChecker;

@Component
@RequiredArgsConstructor
public class DefaultProfanityChecker implements ProfanityChecker {

    private final RestClient restClient;

    @Override
    public boolean isProfanity(final String text) {
        final String response = restClient.get()
                .uri("https://www.purgomalum.com/service/containsprofanity?text={text}", text)
                .retrieve()
                .body(String.class);
        return Boolean.parseBoolean(response);
    }
}
