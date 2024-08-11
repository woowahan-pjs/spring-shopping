package shopping.member.common.exception;

import shopping.common.exception.SpringShoppingException;

public class NotFoundMemberException extends SpringShoppingException {

    public NotFoundMemberException(final String message) {
        super(message);
    }
}
