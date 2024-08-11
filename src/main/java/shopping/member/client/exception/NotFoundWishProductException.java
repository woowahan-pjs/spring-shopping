package shopping.member.client.exception;

import shopping.common.exception.SpringShoppingException;

public class NotFoundWishProductException extends SpringShoppingException {

    public NotFoundWishProductException(final String message) {
        super(message);
    }
}
