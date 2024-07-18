package shopping.product.domain;

public class InvalidProductPriceException extends IllegalArgumentException {

    public InvalidProductPriceException(final String s) {
        super(s);
    }
}
