package shopping.member.domain;

public class PasswordFactory {

    private static final int MIN_LENGTH = 8;

    private final PasswordEncoder passwordEncoder;

    public PasswordFactory(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public Password create(String rawPassword) {
        if (rawPassword == null || rawPassword.length() < MIN_LENGTH) {
            throw new IllegalArgumentException("비밀번호는 8자 이상이어야 합니다.");
        }
        return new Password(passwordEncoder.encode(rawPassword));
    }
}
