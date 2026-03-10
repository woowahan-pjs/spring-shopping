package shopping.domain.product.exception;

import shopping.domain.common.exception.DomainException;

public class InvalidPriceException extends DomainException {
    public InvalidPriceException() {
        super("상품 가격은 0원 이상이어야 합니다.");
    }
}
