package shopping.infra.client.purgomalum;

import shopping.infra.client.RestClientTestConfig;

public final class PurgoMalumClientHelper {

    private PurgoMalumClientHelper() {}

    private static final String BASE_URI = "https://www.purgomalum.com";

    private static class Holder {
        private static final PurgoMalumClientConfig clientConfig = new PurgoMalumClientConfig(RestClientTestConfig.create());
        private static final PurgoMalumClient client = clientConfig.purgomalumClientFactory(BASE_URI);
    }

    public static PurgoMalumClient client() {
        return Holder.client;
    }
}
