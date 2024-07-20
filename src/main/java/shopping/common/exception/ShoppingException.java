package shopping.common.exception;

import org.springframework.http.HttpStatus;

public abstract class ShoppingException extends RuntimeException {

    private final HttpStatus status;

    public ShoppingException(final HttpStatus status, final String message) {
        super(message);
        this.status = status;
    }

    public ShoppingException(final HttpStatus status, final String message, final Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
