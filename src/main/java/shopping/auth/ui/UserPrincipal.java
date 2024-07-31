package shopping.auth.ui;

import java.util.Objects;

public class UserPrincipal {
    public static final Long EMPTY_ID = -1L;
    public static final String EMPTY_MAIL = "";
    private final Long id;
    private final String email;

    public UserPrincipal(final Long id, final String email) {
        this.id = id;
        this.email = email;
    }

    public static UserPrincipal empty() {
        return new UserPrincipal(EMPTY_ID, EMPTY_MAIL);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final UserPrincipal that = (UserPrincipal) o;
        return Objects.equals(id, that.id) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}
