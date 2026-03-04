package shopping.infra.client.purgomalum;

import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
class PurgoMalumClient {

    private final RestClient restClient;

    public <T> String get(final String endPoint, T input) {

        log.info("[HTTP GET 요청] : {}", endPoint + input.toString());

        return restClient
                .get()
                .uri(endPoint + input)
                .accept(MediaType.TEXT_PLAIN)
                .retrieve()
                .body(String.class);
    }
}
