package shopping.common.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import shopping.common.exception.ProfanityServiceUnavailableException;

@Component
@RequiredArgsConstructor
public class PurgoMalumChecker implements ProfanityChecker {
    private final RestTemplate restTemplate;

    @Value("${purgomalum.url}")
    private String url;

    @Override
    public boolean containsProfanity(String text) {
        try {
            String response = restTemplate.getForObject(url, String.class, text);
            return Boolean.parseBoolean(response);
        } catch (RestClientException e) {
            throw new ProfanityServiceUnavailableException("비속어 검증 서비스를 사용할 수 없습니다.", e);
        }
    }
}
