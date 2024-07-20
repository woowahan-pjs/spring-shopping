package shopping.auth;

import shopping.common.BusinessException;
import shopping.common.ErrorCode;

public class RequiresTokenException extends BusinessException {
    protected RequiresTokenException() {
        super(ErrorCode.REQUIRES_TOKEN);
    }
}
