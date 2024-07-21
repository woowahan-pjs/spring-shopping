package shopping.member.application;

import shopping.common.BusinessException;
import shopping.common.ErrorCode;

public class AlreadyRegisteredEmailException extends BusinessException {
    public AlreadyRegisteredEmailException() {
        super(ErrorCode.ALREADY_REGISTERED_EMAIL);
    }
}
