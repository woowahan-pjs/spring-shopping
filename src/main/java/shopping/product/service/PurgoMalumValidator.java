package shopping.product.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Component
public class PurgoMalumValidator implements ProfanityValidator {
    private final String BASE_URL = "https://www.purgomalum.com";
    private final String END_POINT = "/service/containsprofanity?text={text}";

    private final RestClient restClient;

    public PurgoMalumValidator(
            @Value("${purgomalum.connect-timeout-seconds}") int connectionTimeOut,
            @Value("${purgomalum.read-timeout-seconds}") int readTimeOut
    ) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(connectionTimeOut));   // 연결 타임아웃 3초
        factory.setReadTimeout(Duration.ofSeconds(readTimeOut));            // 읽기 타임아웃 5초

        this.restClient = RestClient.builder()
                .baseUrl(BASE_URL)
                .requestFactory(factory)
                .build();
    }

    @Override
    public boolean containsProfanity(String productName) {
        String result = restClient.get()
                .uri(END_POINT, productName)
                .retrieve()
                .body(String.class);

        return Boolean.parseBoolean(result);
    }
}
