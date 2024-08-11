package shopping.common.auth.exception;

import shopping.common.exception.SpringShoppingException;

public class UnAuthenticatedException extends SpringShoppingException {

    public UnAuthenticatedException(final String message) {
        super(message);
    }
}
