package shopping.product.domain;

public class InvalidProductNameException extends IllegalStateException {

    public InvalidProductNameException(String s) {
        super(s);
    }
}
