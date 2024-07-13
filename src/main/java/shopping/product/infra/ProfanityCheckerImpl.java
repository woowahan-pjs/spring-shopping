package shopping.product.infra;

import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import shopping.product.application.ProfanityChecker;

@Component
public class ProfanityCheckerImpl implements ProfanityChecker {
    private static final Logger log = LoggerFactory.getLogger(ProfanityCheckerImpl.class);

    private final RestClient purgomalumRestClient;

    public ProfanityCheckerImpl(RestClient purgomalumRestClient) {
        this.purgomalumRestClient = purgomalumRestClient;
    }

    /**
     * @param value 검사 하고자 하는 텍스트
     * @return 텍스트를 보내면 Purgomalum에서 true or false를 내려준다.
     */
    @Override
    public boolean check(String value) {
        String responseBody = purgomalumRestClient.get()
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
