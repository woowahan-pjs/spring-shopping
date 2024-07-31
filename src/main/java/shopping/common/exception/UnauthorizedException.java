package shopping.common.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends ShoppingException {
    public UnauthorizedException(final String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }

    public UnauthorizedException(final String message, final Throwable cause) {
        super(HttpStatus.UNAUTHORIZED, message, cause);
    }
}
