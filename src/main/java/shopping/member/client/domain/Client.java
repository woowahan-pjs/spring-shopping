package shopping.member.client.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import shopping.member.common.domain.Member;
import shopping.member.common.domain.Password;

@Entity
public class Client extends Member {

    @Embedded
    private WishProducts wishProducts = new WishProducts();

    protected Client() {
    }

    public Client(final String email, final Password password, final String memberName) {
        super(email, password, memberName);
    }

    public void wish(final WishProduct wishProduct) {
        wishProducts.wish(wishProduct);
    }

    public void unWish(final WishProduct wishProduct) {
        wishProducts.unWish(wishProduct);
    }
}
