package shopping.product.exception;

public class InvalidProductPriceException extends IllegalArgumentException {

    public InvalidProductPriceException(final String s) {
        super(s);
    }
}
