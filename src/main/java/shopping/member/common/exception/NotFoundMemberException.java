package shopping.member.common.exception;

public class NotFoundMemberException extends RuntimeException {

    public NotFoundMemberException(final String message) {
        super(message);
    }
}
