package shopping.common.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class PurgoMalumValidator implements ProductNameValidator {
    private final RestTemplate restTemplate;

    @Value("${purgomalum.url}")
    private String url;

    @Override
    public boolean containsProfanity(String text) {
        String response = restTemplate.getForObject(url, String.class, text);
        return Boolean.parseBoolean(response);
    }
}
