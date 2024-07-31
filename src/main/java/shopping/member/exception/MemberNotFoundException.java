package shopping.member.exception;

import shopping.common.exception.NotFoundException;

public class MemberNotFoundException extends NotFoundException {
    public MemberNotFoundException(final String message) {
        super(message);
    }
}
