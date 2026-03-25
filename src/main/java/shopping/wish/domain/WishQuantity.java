package shopping.wish.domain;

import shopping.common.ApiException;
import shopping.common.ErrorCode;

public record WishQuantity(int value) {
    public WishQuantity {
        if (value <= 0) {
            throw new ApiException(ErrorCode.WISH_QUANTITY_INVALID);
        }
    }

    public static WishQuantity from(Integer requestedQuantity) {
        if (requestedQuantity == null) {
            return new WishQuantity(1);
        }
        return new WishQuantity(requestedQuantity);
    }
}
