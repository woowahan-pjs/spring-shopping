package shopping.product;

import shopping.product.domain.ProfanityChecker;
import shopping.product.domain.ProfanityCheckTimeoutException;

import java.net.SocketTimeoutException;
import java.net.http.HttpTimeoutException;
import java.time.Duration;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

@Component
public class PurgoMalumProfanityChecker implements ProfanityChecker {

    private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(1);
    private static final Duration READ_TIMEOUT = Duration.ofSeconds(1);

    private final RestClient restClient;

    public PurgoMalumProfanityChecker(RestClient.Builder builder) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(CONNECT_TIMEOUT);
        factory.setReadTimeout(READ_TIMEOUT);
        this.restClient =
                builder.requestFactory(factory).baseUrl("https://www.purgomalum.com").build();
    }

    @Override
    public boolean containsProfanity(String text) {
        try {
            String result = restClient.get().uri("/service/containsprofanity?text={text}", text)
                    .retrieve().body(String.class);
            return "true".equals(result);
        } catch (ResourceAccessException e) {
            if (isTimeout(e.getCause())) {
                throw new ProfanityCheckTimeoutException(e);
            }
            throw e;
        }
    }

    private boolean isTimeout(Throwable cause) {
        return cause instanceof SocketTimeoutException || cause instanceof HttpTimeoutException;
    }
}
