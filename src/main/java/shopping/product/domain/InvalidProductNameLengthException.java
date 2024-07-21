package shopping.product.domain;

import shopping.common.BusinessException;
import shopping.common.ErrorCode;

public class InvalidProductNameLengthException extends BusinessException {
    public InvalidProductNameLengthException() {
        super(ErrorCode.INVALID_PRODUCT_NAME_LENGTH);
    }
}
