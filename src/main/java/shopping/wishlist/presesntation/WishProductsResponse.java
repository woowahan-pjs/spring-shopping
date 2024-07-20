package shopping.wishlist.presesntation;

import java.util.List;
import shopping.wishlist.domain.WishProduct;

public record WishProductsResponse(List<WishProductResponse> wishProducts) {
    public static WishProductsResponse from(List<WishProduct> wishProducts) {
        List<WishProductResponse> wishProductResponses = wishProducts.stream()
                .map(WishProductResponse::from)
                .toList();

        return new WishProductsResponse(wishProductResponses);
    }
}
