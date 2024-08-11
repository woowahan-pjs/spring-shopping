package shopping.member.common.exception;

import shopping.common.exception.SpringShoppingException;

public class InvalidMemberException extends SpringShoppingException {

    public InvalidMemberException(final String message) {
        super(message);
    }
}
