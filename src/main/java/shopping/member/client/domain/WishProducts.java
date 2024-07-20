package shopping.member.client.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import shopping.member.client.exception.NotFoundWishProductException;

@Embeddable
public class WishProducts {

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "email", nullable = false)
    private List<WishProduct> wishProducts = new ArrayList<>();

    protected WishProducts() {
    }

    public void wish(WishProduct addWishProduct) {
        validateAddWishItem(addWishProduct);
        this.wishProducts.add(addWishProduct);
    }

    private void validateAddWishItem(final WishProduct addWishProduct) {
        final boolean match = this.wishProducts.stream()
                .anyMatch(wishProduct -> wishProduct.isSameProduct(addWishProduct));
        if (match) {
            throw new DuplicateWishProductException(
                    "중복되는 상품에 하트를 누를 수 없습니다. " + addWishProduct.getProductId());
        }
    }

    public void unWish(WishProduct delWishProduct) {
        final boolean removed = this.wishProducts.removeIf(
                wishProduct -> wishProduct.isSameProduct(delWishProduct));
        if (!removed) {
            throw new NotFoundWishProductException(
                    "해당 위시상품을 찾을 수 없습니다. " + delWishProduct.getProductId());
        }
    }
}
