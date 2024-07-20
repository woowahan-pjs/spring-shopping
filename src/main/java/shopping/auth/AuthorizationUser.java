package shopping.auth;

public class AuthorizationUser {
    private final long userId;

    public AuthorizationUser(final long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }
}
