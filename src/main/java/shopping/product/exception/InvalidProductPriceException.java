package shopping.product.exception;

import shopping.common.exception.SpringShoppingException;

public class InvalidProductPriceException extends SpringShoppingException {

    public InvalidProductPriceException(final String s) {
        super(s);
    }
}
