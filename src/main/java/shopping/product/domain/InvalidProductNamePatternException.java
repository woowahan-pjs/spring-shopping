package shopping.product.domain;

import shopping.common.BusinessException;
import shopping.common.ErrorCode;

public class InvalidProductNamePatternException extends BusinessException {
    protected InvalidProductNamePatternException() {
        super(ErrorCode.INVALID_PRODUCT_NAME_PATTERN);
    }
}
