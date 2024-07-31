package shopping.wishilist.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import shopping.product.domain.Product;
import shopping.wishilist.exception.InvalidWishlistException;

import java.util.Collections;
import java.util.List;

@Entity
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Embedded
    private WishlistItems wishlistItems = new WishlistItems();

    public Wishlist(final Long memberId) {
        if (memberId == null) {
            throw new InvalidWishlistException("wishlist 생성시 memberId 는 필수값입니다.");
        }
        this.memberId = memberId;
    }

    protected Wishlist() {
    }

    public void add(final Product product) {
        wishlistItems.add(product);
    }

    public void remove(final Product product) {
        wishlistItems.remove(product);
    }

    public Long getId() {
        return id;
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(wishlistItems.getProducts());
    }
}
