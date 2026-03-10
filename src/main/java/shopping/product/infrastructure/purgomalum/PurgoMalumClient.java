package shopping.product.infrastructure.purgomalum;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClient;
import shopping.common.exception.ApiException;
import shopping.common.exception.ErrorType;
import shopping.product.domain.ProfanityChecker;

@Component
@RequiredArgsConstructor
public class PurgoMalumClient implements ProfanityChecker {

    private static final String BASE_URL = "https://www.purgomalum.com/service/containsprofanity";

    private final RestClient restClient;

    @Override
    public boolean containsProfanity(String text) {
        try {
            String result = restClient.get()
                .uri(BASE_URL + "?text={text}", text)
                .retrieve()
                .body(String.class);
            return Boolean.parseBoolean(result);
        } catch (RestClientException e) {
            throw new ApiException(ErrorType.EXTERNAL_API_ERROR.getDescription(),
                ErrorType.EXTERNAL_API_ERROR, HttpStatus.BAD_GATEWAY);
        }
    }
}
