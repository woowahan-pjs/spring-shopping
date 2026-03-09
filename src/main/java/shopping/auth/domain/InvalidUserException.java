package shopping.auth.domain;

import shopping.infra.exception.ShoppingBusinessException;

public class InvalidUserException extends ShoppingBusinessException {
    public InvalidUserException() {
        super("이메일 또는 비밀번호를 확인해주세요.");
    }
}
