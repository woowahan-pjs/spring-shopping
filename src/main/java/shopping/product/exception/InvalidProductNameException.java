package shopping.product.exception;

import shopping.common.exception.SpringShoppingException;

public class InvalidProductNameException extends SpringShoppingException {

    public InvalidProductNameException(String s) {
        super(s);
    }
}
