package shopping.common.auth.token;

@FunctionalInterface
public interface TokenGenerator {

    String generate(String email, String role);
}
