package shopping.entity;

import lombok.Getter;

import java.util.Objects;

@Getter
public class ShoppingToken {
    private final Long id;
    private final String email;

    public ShoppingToken(final Long id, final String email) {
        this.id = id;
        this.email = email;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        final ShoppingToken shoppingToken = (ShoppingToken) object;
        return Objects.equals(id, shoppingToken.id) && Objects.equals(email, shoppingToken.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}
