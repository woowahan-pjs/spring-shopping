package shopping.product.application;

public class NotFoundProductException extends RuntimeException {

    public NotFoundProductException(final String message) {
        super(message);
    }
}
