package shopping.product.application;

import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ProfanityCheckService {
    private static final Logger log = LoggerFactory.getLogger(ProfanityCheckService.class);

    private final RestClient purgomalumRestClient;

    public ProfanityCheckService(RestClient purgomalumRestClient) {
        this.purgomalumRestClient = purgomalumRestClient;
    }

    /**
     * @param text 검사 하고자 하는 텍스트
     * @return 텍스트를 보내면 Purgomalum에서 true or false를 내려준다.
     */
    public boolean containsProfanity(String text) {
        Boolean responseBody = purgomalumRestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/containsprofanity")
                        .queryParam("text", text)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError,
                        ((request, response) -> log.error("[Purgomalum] Internal Server Error")))
                .body(Boolean.class);

        if (Objects.isNull(responseBody)) {
            log.warn("[Purgomalum] body 응답이 null입니다.");
        }

        return Objects.nonNull(responseBody) && responseBody;
    }
}
