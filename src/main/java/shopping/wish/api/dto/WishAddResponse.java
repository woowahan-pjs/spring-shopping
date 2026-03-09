package shopping.wish.api.dto;

import shopping.wish.service.dto.WishAddOutput;

public record WishAddResponse(Long id, Long productId, String productName) {

    public static WishAddResponse from(WishAddOutput output) {
        return new WishAddResponse(output.id(), output.productId(), output.productName());
    }
}
