package shopping.member.client.exception;

import shopping.common.exception.SpringShoppingException;

public class DuplicateWishProductException extends SpringShoppingException {

    public DuplicateWishProductException(final String message) {
        super(message);
    }
}
