package shopping.member.client.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import java.util.List;
import lombok.Getter;
import shopping.member.common.domain.Member;
import shopping.member.common.domain.Password;

@Entity
public class Client extends Member {

    @Embedded
    @Getter
    private WishProducts wishProducts = new WishProducts();

    protected Client() {
    }

    public Client(final String email, final Password password, final String memberName) {
        super(email, password, memberName);
    }

    public void wish(final Long productId) {
        wishProducts.wish(productId);
    }

    public void unWish(final Long productId) {
        wishProducts.unWish(productId);
    }

    public List<Long> productIds() {
        return wishProducts.productIds();
    }
}
