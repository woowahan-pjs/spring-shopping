package shopping.member.application;

import shopping.common.BusinessException;
import shopping.common.ErrorCode;

public class MemberNotFoundException extends BusinessException {
    public MemberNotFoundException(String email) {
        super(ErrorCode.MEMBER_NOT_FOUND, "사용자를 찾을 수 없음 email=" + email);
    }
}
