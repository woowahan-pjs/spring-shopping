package shopping.wishproduct.presesntation;

import shopping.wishproduct.domain.WishProduct;

public record WishProductResponse(
        Long id,
        String name,
        String imageUrl,
        Integer price
) {
    public static WishProductResponse from(WishProduct wishProduct) {
        return new WishProductResponse(
                wishProduct.getProductId(),
                wishProduct.getName(),
                wishProduct.getImageUrl(),
                wishProduct.getPrice()
        );
    }
}
