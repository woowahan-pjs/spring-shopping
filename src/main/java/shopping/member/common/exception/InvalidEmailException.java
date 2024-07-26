package shopping.member.common.exception;

public class InvalidEmailException extends RuntimeException {

    public InvalidEmailException(final String message) {
        super(message);
    }
}
