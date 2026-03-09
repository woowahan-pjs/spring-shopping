package shopping.wish.api.dto;

import shopping.wish.service.dto.WishOutput;

public record WishResponse(Long id, Long productId, String productName, Long price, String imageUrl) {

    public static WishResponse from(WishOutput output) {
        return new WishResponse(
                output.id(),
                output.productId(),
                output.productName(),
                output.price(),
                output.imageUrl()
        );
    }
}
