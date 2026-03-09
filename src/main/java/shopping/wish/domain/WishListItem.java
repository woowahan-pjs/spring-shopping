package shopping.wish.domain;

import jakarta.persistence.*;
import shopping.product.domain.Product;

import java.time.LocalDateTime;

@Entity
public class WishListItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wishlist_id")
    private WishList wishList;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private LocalDateTime addedAt;

    protected WishListItem() {

    }

    private WishListItem(WishList wishList, Product product) {
        this.wishList = wishList;
        this.product = product;
        this.addedAt = LocalDateTime.now();
    }

    public static WishListItem create(WishList wishList, Product product) {
        return new WishListItem(wishList, product);
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }
}
