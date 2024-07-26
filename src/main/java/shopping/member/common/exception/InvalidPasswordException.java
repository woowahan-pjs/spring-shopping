package shopping.member.common.exception;

public class InvalidPasswordException extends RuntimeException {

    public InvalidPasswordException(final String message) {
        super(message);
    }
}
