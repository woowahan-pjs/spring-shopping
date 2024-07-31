package shopping.auth.application.dto;

import java.util.Objects;

public class TokenInfo {
    private final Long id;
    private final String email;

    public TokenInfo(final Long id, final String email) {
        this.id = id;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        final TokenInfo tokenInfo = (TokenInfo) object;
        return Objects.equals(id, tokenInfo.id) && Objects.equals(email, tokenInfo.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}
