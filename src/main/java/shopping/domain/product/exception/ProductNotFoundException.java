package shopping.domain.product.exception;

import shopping.domain.common.exception.DomainException;

public class ProductNotFoundException extends DomainException {
    public ProductNotFoundException() {
        super("상품을 찾을 수 없습니다.");
    }

    public ProductNotFoundException(String message) {
        super(message);
    }
}
