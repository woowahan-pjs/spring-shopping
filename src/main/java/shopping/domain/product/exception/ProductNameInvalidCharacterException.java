package shopping.domain.product.exception;

import shopping.domain.common.exception.DomainException;

public class ProductNameInvalidCharacterException extends DomainException {
    public ProductNameInvalidCharacterException() {
        super("이름에 허용되지 않는 특수문자가 포함되어 있습니다.");
    }

    public ProductNameInvalidCharacterException(String message) {
        super(message);
    }
}
