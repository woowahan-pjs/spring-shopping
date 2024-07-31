package shopping.product.exception;

import shopping.common.exception.NotFoundException;

public class ProductNotExistException extends NotFoundException {
    public ProductNotExistException(final String message) {
        super(message);
    }
}
