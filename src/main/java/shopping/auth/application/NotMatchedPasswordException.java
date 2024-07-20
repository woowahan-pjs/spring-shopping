package shopping.auth.application;

import shopping.common.BusinessException;
import shopping.common.ErrorCode;

public class NotMatchedPasswordException extends BusinessException {
    protected NotMatchedPasswordException() {
        super(ErrorCode.NOT_MATCHED_PASSWORD);
    }
}
