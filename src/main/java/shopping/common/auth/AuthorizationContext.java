package shopping.common.auth;

public class AuthorizationContext {
    private static final ThreadLocal<AuthorizationUser> authorizationContext = new InheritableThreadLocal<>();

    public static AuthorizationUser get() {
        return authorizationContext.get();
    }

    public static void set(final AuthorizationUser authorizationUser) {
        authorizationContext.set(authorizationUser);
    }

    public static void clear() {
        authorizationContext.remove();
    }
}
