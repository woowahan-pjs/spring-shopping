package shopping.domain.member.exception;

import shopping.domain.common.exception.NotFoundException;

public class MemberNotFoundException extends NotFoundException {
    private static final String MESSAGE_FORMAT = "존재하지 않는 회원입니다. (ID: %d)";

    public MemberNotFoundException(Long memberId) {
        super(String.format(MESSAGE_FORMAT, memberId));
    }
}
