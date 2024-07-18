package shopping.member.client.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class WishProducts {

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "email", nullable = false)
    private List<WishProduct> wishProducts = new ArrayList<>();

    protected WishProducts(){
    }

    public void wish(WishProduct wishProduct) {
        this.wishProducts.add(wishProduct);
    }

    public void unWish(WishProduct wishProduct) {
        this.wishProducts.removeIf(delWishProduct -> delWishProduct.isSameProduct(wishProduct));
    }
}
