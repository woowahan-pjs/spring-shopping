package shopping.product.application;

import shopping.common.BusinessException;
import shopping.common.ErrorCode;

public class ProductNotFoundException extends BusinessException {
    protected ProductNotFoundException() {
        super(ErrorCode.PRODUCT_NOT_FOUND);
    }
}
