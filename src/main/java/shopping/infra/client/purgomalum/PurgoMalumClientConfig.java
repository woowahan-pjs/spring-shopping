package shopping.infra.client.purgomalum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
class PurgoMalumClientConfig {

    private final RestClient.Builder restClientBuilder;

    @Bean
    public PurgoMalumClient purgomalumClientFactory(
            @Value("${api.purgomalum.base-uri}") final String baseUri) {
        return new PurgoMalumClient(restClientBuilder.baseUrl(baseUri).build());
    }
}
