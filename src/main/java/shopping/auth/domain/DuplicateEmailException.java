package shopping.auth.domain;

import shopping.infra.exception.ShoppingBusinessException;

public class DuplicateEmailException extends ShoppingBusinessException {

    public DuplicateEmailException() {
        super("이미 등록된 이메일입니다.");
    }
}
