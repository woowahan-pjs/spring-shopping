package shopping.product.application;

import shopping.common.BusinessException;
import shopping.common.ErrorCode;

public class ContainsProfanityException extends BusinessException {
    protected ContainsProfanityException() {
        super(ErrorCode.CONTAINS_PROFANITY);
    }
}
