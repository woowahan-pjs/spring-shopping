package shopping.member.client.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import shopping.member.client.exception.DuplicateWishProductException;
import shopping.member.client.exception.NotFoundWishProductException;

@Embeddable
public class WishProducts {

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "email", nullable = false)
    private List<WishProduct> wishProducts = new ArrayList<>();

    protected WishProducts() {
    }

    public void wish(Long productId) {
        validateAddWishItem(productId);
        this.wishProducts.add(new WishProduct(productId));
    }

    private void validateAddWishItem(final Long productId) {
        final boolean match = this.wishProducts.stream()
                .anyMatch(wishProduct -> wishProduct.isSameProduct(productId));
        if (match) {
            throw new DuplicateWishProductException(
                    "중복되는 상품에 하트를 누를 수 없습니다. " + productId);
        }
    }

    public void unWish(Long productId) {
        final boolean removed = this.wishProducts.removeIf(
                wishProduct -> wishProduct.isSameProduct(productId));
        if (!removed) {
            throw new NotFoundWishProductException(
                    "해당 위시상품을 찾을 수 없습니다. " + productId);
        }
    }

    public List<Long> productIds() {
        return wishProducts.stream()
                .map(WishProduct::getProductId)
                .toList();
    }
}
