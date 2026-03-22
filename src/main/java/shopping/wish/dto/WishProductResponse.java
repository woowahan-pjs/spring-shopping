package shopping.wish.dto;

import shopping.product.domain.Product;
import shopping.wish.domain.WishListItem;

public class WishProductResponse {

    private Long wishId;
    private Long productId;
    private String name;
    private long price;
    private String imageUrl;

    public WishProductResponse(Long wishId, Long productId, String name, long price, String imageUrl) {
        this.wishId = wishId;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static WishProductResponse from(WishListItem item) {

        Product product = item.getProduct();

        return new WishProductResponse(
                item.getId(),
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl()
        );
    }

    public Long getWishId() {
        return wishId;
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
