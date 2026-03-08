package shopping.domain.product.exception;

import shopping.domain.common.exception.DomainException;

public class ProfanityNameException extends DomainException {
    private final String invalidName;

    public ProfanityNameException(String ivalidName) {
        super(String.format("상품 이름에 비속어가 포함되어 있습니다: [%s]", ivalidName));
        this.invalidName = ivalidName;
    }
}
