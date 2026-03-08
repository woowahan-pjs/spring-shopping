package shopping.service;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class PurgoMalumValidator implements ProfanityValidator {
    private final String PURGOMALUM_URL = "https://www.purgomalum.com/service/containsprofanity?text={text}";

    private final RestClient restClient;

    public PurgoMalumValidator() {
        this.restClient = RestClient.create();
    }

    @Override
    public boolean containsProfanity(String productName) {
        String result = restClient.get()
                .uri(PURGOMALUM_URL, productName)
                .retrieve()
                .body(String.class);
        return Boolean.parseBoolean(result);
    }
}
