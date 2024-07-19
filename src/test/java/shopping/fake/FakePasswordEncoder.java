package shopping.fake;

import shopping.member.common.domain.PasswordEncoder;

public class FakePasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(final String rawPassword) {
        return reversePassword(rawPassword);
    }

    @Override
    public boolean matches(final String rawPassword, final String password) {
        return reversePassword(rawPassword).equals(password);
    }

    private String reversePassword(final String rawPassword) {
        final StringBuilder stringBuilder = new StringBuilder(rawPassword);
        return stringBuilder.reverse().toString();
    }
}
