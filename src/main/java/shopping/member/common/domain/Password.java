package shopping.member.common.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Password {

    @Column(name = "password", nullable = false)
    private String password;

    protected Password() {
    }

    public Password(final String rawPassword, final PasswordEncoder encoder) {
        this.password = encoder.encode(rawPassword);
    }

    public boolean isMatch(final String rawPassword, final PasswordEncoder encoder) {
        return encoder.matches(rawPassword, password);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
