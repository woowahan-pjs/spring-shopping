package shopping.product.exception;

import shopping.common.exception.BadRequestException;

public class InvalidProductException extends BadRequestException {
    public InvalidProductException(final String message) {
        super(message);
    }
}
