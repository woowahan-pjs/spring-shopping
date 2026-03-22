package shopping.common.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException() {
        super("존재하지 않는 상품입니다.");
    }
}
