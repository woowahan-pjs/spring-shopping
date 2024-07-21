package shopping.token.domain;

import shopping.common.BusinessException;
import shopping.common.ErrorCode;

public class InvalidTokenException extends BusinessException {
    public InvalidTokenException() {
        super(ErrorCode.INVALID_TOKEN);
    }
}
