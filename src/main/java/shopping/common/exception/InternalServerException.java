package shopping.common.exception;

import org.springframework.http.HttpStatus;

public class InternalServerException extends ShoppingException {
    public InternalServerException(final String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public InternalServerException(final String message, final Throwable cause) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message, cause);
    }
}
