package shopping.infra.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import shopping.infra.exception.ShoppingBusinessException;
import shopping.infra.exception.ShoppingServerException;

@Slf4j
@Configuration
@RequiredArgsConstructor
class RestClientConfig {

    private static final int DEFAULT_TIMEOUT_MS = 30_000;

    @Bean
    public RestClient.Builder restClientBuilderConfig() {
        final SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(DEFAULT_TIMEOUT_MS);
        requestFactory.setReadTimeout(DEFAULT_TIMEOUT_MS);

        return RestClient.builder()
            .requestFactory(requestFactory)
            .defaultStatusHandler(statusCode -> statusCode.is4xxClientError() || statusCode.is5xxServerError(), (request, response) -> {
                log.error("[외부 HTTP 요청 실패 ({})] : [{}] {}, {}", response.getStatusCode(), request.getMethod(), request.getURI(), response.getStatusText());

                if (response.getStatusCode().is4xxClientError()) {
                    throw new ShoppingBusinessException();
                }

                if (response.getStatusCode().is5xxServerError()) {
                    throw new ShoppingServerException();
                }

                throw new ShoppingBusinessException();
            });
    }
}
