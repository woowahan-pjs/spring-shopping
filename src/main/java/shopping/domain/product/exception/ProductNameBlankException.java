package shopping.domain.product.exception;

import shopping.domain.common.exception.DomainException;

public class ProductNameBlankException extends DomainException {
    public ProductNameBlankException() {
        super("상품 이름은 필수입니다.");
    }

    public ProductNameBlankException(String message) {
        super(message);
    }
}
