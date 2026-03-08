package shopping.member;

public class FakePasswordEncoder implements PasswordEncoder {

    private static final String PREFIX = "encoded:";

    @Override
    public String encode(String rawPassword) {
        return PREFIX + rawPassword;
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return encodedPassword.equals(PREFIX + rawPassword);
    }
}
