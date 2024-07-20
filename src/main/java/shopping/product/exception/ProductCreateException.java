package shopping.product.exception;

public class ProductCreateException extends RuntimeException {
    public ProductCreateException(final String message) {
        super(message);
    }
}
