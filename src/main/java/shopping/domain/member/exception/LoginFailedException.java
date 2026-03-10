package shopping.domain.member.exception;

import shopping.domain.common.exception.DomainException;

public class LoginFailedException extends DomainException {
    private static final String MESSAGE = "이메일 또는 비밀번호가 일치하지 않습니다.";

    public LoginFailedException() {
        super(MESSAGE);
    }

    // 보안을 위해 이메일 정보를 로그에는 남기되, 사용자에게는 공통 메시지만 노출합니다.
    public LoginFailedException(String email) {
        super(MESSAGE + " (입력 이메일: " + email + ")");
    }
}
