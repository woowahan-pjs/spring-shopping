package shopping.member.client.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import shopping.member.common.domain.Member;
import shopping.member.common.domain.MemberPriority;
import shopping.member.common.domain.Password;
import shopping.member.common.domain.PasswordEncoder;

@Entity
public class Client extends Member {

    @Embedded
    private WishProducts wishProducts = new WishProducts();

    protected Client() {
    }

    public Client(String email, String rawPassword, PasswordEncoder encoder, String memberName) {
        super(email, new Password(rawPassword, encoder), memberName, MemberPriority.CLIENT);
    }

    public void wish(WishProduct wishProduct) {
        wishProducts.wish(wishProduct);
    }

    public void unWish(WishProduct wishProduct) {
        wishProducts.unWish(wishProduct);
    }
}
