package shopping.member.common.exception;

import shopping.common.exception.SpringShoppingException;

public class InvalidPasswordException extends SpringShoppingException {

    public InvalidPasswordException(final String message) {
        super(message);
    }
}
