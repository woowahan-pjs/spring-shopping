package shopping.member.exception;

import shopping.common.exception.BadRequestException;

public class InvalidMemberException extends BadRequestException {
    public InvalidMemberException(final String message) {
        super(message);
    }
}
