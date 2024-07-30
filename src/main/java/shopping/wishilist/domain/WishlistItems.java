package shopping.wishilist.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import shopping.product.domain.Product;
import shopping.wishilist.exception.InvalidWishlistException;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

@Embeddable
public class WishlistItems implements Iterable<Product> {
    @OneToMany
    @JoinColumn(name = "wishlist_id")
    private List<Product> products = new LinkedList<>();

    protected WishlistItems() {
    }

    public void add(final Product product) {
        if (products.contains(product)) {
            throw new InvalidWishlistException("이미 존재하는 상품은 추가할 수 없습니다.");
        }

        this.products.add(product);
    }

    public void remove(final Product product) {
        this.products.remove(product);
    }

    public Stream<Product> stream() {
        return products.stream();
    }

    @Override
    public Iterator<Product> iterator() {
        return products.iterator();
    }
}
