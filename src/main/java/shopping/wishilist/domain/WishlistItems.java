package shopping.wishilist.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import shopping.product.domain.Product;
import shopping.wishilist.exception.InvalidWishlistException;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Embeddable
public class WishlistItems implements Iterable<WishlistItem> {
    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name = "wishlist_id")
    private List<WishlistItem> wishlistItems = new LinkedList<>();

    protected WishlistItems() {
    }

    public void add(final Product product) {
        if (contains(product)) {
            throw new InvalidWishlistException("이미 존재하는 상품은 추가할 수 없습니다.");
        }
        this.wishlistItems.add(new WishlistItem(product));
    }

    private boolean contains(final Product product) {
        return wishlistItems.stream()
                .anyMatch(item -> item.isSameProduct(product));
    }

    public void remove(final Product product) {
        this.wishlistItems.removeIf(item -> item.isSameProduct(product));
    }

    public List<Product> getProducts() {
        return wishlistItems.stream()
                .map(WishlistItem::getProduct)
                .toList();
    }

    @Override
    public Iterator<WishlistItem> iterator() {
        return wishlistItems.iterator();
    }
}
