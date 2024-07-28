package shopping.common.exception;

import jakarta.persistence.PersistenceException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomerExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomerExceptionHandler.class);

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorMessage> constraintViolation(final ConstraintViolationException exception) {
        logger.info("ConstraintViolationException = {}", exception.getMessage());
        return ResponseEntity.badRequest().body(new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(BusinessRuntimeException.class)
    public ResponseEntity<ErrorMessage> businessRuntime(final BusinessRuntimeException exception) {
        logger.info("BusinessRuntimeException = {}", exception.getMessage());
        return ResponseEntity.badRequest().body(new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<ErrorMessage> persistence(final PersistenceException exception) {
        logger.info("PersistenceException = {}", exception.getStackTrace());
        return ResponseEntity.badRequest().body(new ErrorMessage(exception.getMessage()));
    }

    class ErrorMessage {
        private String message;

        public ErrorMessage() {
        }

        public ErrorMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
