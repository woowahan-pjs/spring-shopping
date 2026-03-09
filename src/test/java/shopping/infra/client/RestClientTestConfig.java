package shopping.infra.client;

import org.springframework.web.client.RestClient;

public final class RestClientTestConfig {

    private static final RestClientConfig restClientConfig = new RestClientConfig();

    public static RestClient.Builder create() {
        return restClientConfig.restClientBuilderConfig();
    }
}
