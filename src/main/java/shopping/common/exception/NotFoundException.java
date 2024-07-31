package shopping.common.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ShoppingException {
    public NotFoundException(final String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

    public NotFoundException(final String message, final Throwable cause) {
        super(HttpStatus.NOT_FOUND, message, cause);
    }
}
