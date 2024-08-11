package shopping.member.common.exception;

import shopping.common.exception.SpringShoppingException;

public class InvalidEmailException extends SpringShoppingException {

    public InvalidEmailException(final String message) {
        super(message);
    }
}
