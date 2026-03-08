package shopping.domain.product.exception;

import shopping.domain.common.exception.DomainException;

public class ProductNameLengthExceededException extends DomainException {
    public ProductNameLengthExceededException(int maxLength) {
        super(String.format("상품 이름은 공백을 포함하여 최대 %d자까지 입력할 수 있습니다.", maxLength));
    }

    public ProductNameLengthExceededException(String message) {
        super(message);
    }
}
