package shopping.wishilist.exception;

import shopping.common.exception.BadRequestException;

public class InvalidWishlistException extends BadRequestException {
    public InvalidWishlistException(final String message) {
        super(message);
    }
}
