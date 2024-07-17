package shopping.member.common.domain;

public interface PasswordEncoder {

    String encode(final String rawPassword);

    boolean matches(final String rawPassword, final String password);
}
