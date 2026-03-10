package shopping.domain.member.exception;

import shopping.domain.common.exception.DomainException;

public class DuplicateEmailException extends DomainException {
    public DuplicateEmailException(String email) {
        super(String.format("이미 가입된 이메일입니다: [%s]", email));
    }
}
