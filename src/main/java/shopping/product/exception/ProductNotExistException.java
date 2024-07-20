package shopping.product.exception;

public class ProductNotExistException extends RuntimeException {
    public ProductNotExistException(final String message) {
        super(message);
    }
}
