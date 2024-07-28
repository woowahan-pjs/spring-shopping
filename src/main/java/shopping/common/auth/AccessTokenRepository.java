package shopping.common.auth;

public interface AccessTokenRepository {
    boolean exists(AuthorizationType authorizationType, String accessToken);
    long find(AuthorizationType authorizationType, String accessToken);

    String create(final AuthorizationType authorizationType, final long id);

    void delete(AuthorizationType authorizationType, long userId);
}
