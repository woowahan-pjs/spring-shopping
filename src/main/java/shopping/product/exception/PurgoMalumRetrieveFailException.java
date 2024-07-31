package shopping.product.exception;

import shopping.common.exception.InternalServerException;

public class PurgoMalumRetrieveFailException extends InternalServerException {
    public PurgoMalumRetrieveFailException(final String message) {
        super(message);
    }
}
