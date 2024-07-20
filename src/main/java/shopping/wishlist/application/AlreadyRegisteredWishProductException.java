package shopping.wishlist.application;

import shopping.common.BusinessException;
import shopping.common.ErrorCode;

public class AlreadyRegisteredWishProductException extends BusinessException {
    protected AlreadyRegisteredWishProductException() {
        super(ErrorCode.ALREADY_REGISTERED_WISH_PRODUCT);
    }
}
