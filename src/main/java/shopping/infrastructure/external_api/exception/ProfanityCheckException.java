package shopping.infrastructure.external_api.exception;

public class ProfanityCheckException extends RuntimeException {

    public ProfanityCheckException(final String message) {
        super(message);
    }
}