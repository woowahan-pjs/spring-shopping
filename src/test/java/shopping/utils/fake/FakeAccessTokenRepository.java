package shopping.utils.fake;

import shopping.auth.AccessTokenRepository;
import shopping.auth.AuthorizationType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FakeAccessTokenRepository implements AccessTokenRepository {
    private final Map<String, String> storage = new HashMap<>();

    @Override
    public boolean exists(final AuthorizationType authorizationType, final String accessToken) {
        return false;
    }

    @Override
    public long find(final AuthorizationType authorizationType, final String accessToken) {
        return 0;
    }

    @Override
    public String create(final AuthorizationType authorizationType, final long id) {
        final String accessToken = UUID.randomUUID().toString();
        storage.put(authorizationType.name() + accessToken, String.valueOf(id));
        return accessToken;
    }
}
