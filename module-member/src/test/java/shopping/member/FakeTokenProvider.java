package shopping.member;

public class FakeTokenProvider implements TokenProvider {

    private static final String PREFIX = "token:";

    @Override
    public String createToken(String email) {
        return PREFIX + email;
    }

    @Override
    public String extractEmail(String token) {
        return token.substring(PREFIX.length());
    }
}
