package shopping.product.exception;

import shopping.common.exception.SpringShoppingException;

public class NotFoundProductException extends SpringShoppingException {

    public NotFoundProductException(final String message) {
        super(message);
    }
}
