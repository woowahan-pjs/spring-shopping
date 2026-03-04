package shopping.infra.client;

import org.springframework.web.client.RestClient;

public class RestClientTestConfig {

    private static RestClientConfig restClientConfig = new RestClientConfig();

    public static RestClient.Builder create() {
        return restClientConfig.restClientBuilderConfig();
    }
}
