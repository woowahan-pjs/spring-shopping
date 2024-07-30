package shopping.wishilist.application.dto;

import shopping.product.application.dto.ProductResponse;
import shopping.wishilist.domain.Wishlist;

import java.util.List;

public class WishlistResponse {

    private Long id;
    private List<ProductResponse> products;

    public WishlistResponse() {
    }

    public WishlistResponse(final Long id, final List<ProductResponse> products) {
        this.id = id;
        this.products = products;
    }

    public static WishlistResponse from(final Wishlist wishlist) {
        return new WishlistResponse(
                wishlist.getId(),
                wishlist.getProducts()
                        .stream()
                        .map(ProductResponse::from)
                        .toList()
        );
    }

    public Long getId() {
        return id;
    }

    public List<ProductResponse> getProducts() {
        return products;
    }
}
