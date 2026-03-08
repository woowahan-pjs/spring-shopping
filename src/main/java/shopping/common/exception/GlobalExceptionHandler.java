package shopping.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shopping.common.exception.response.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidProductNameException.class)
    public ResponseEntity<ErrorResponse> handleInvalidProductName(InvalidProductNameException e) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("INVALID_PRODUCT_NAME", e.getMessage()));
    }

    @ExceptionHandler(LoginFailedException.class)
    public ResponseEntity<ErrorResponse> handleLoginFailed(LoginFailedException e) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("LOGIN_FAILED", e.getMessage()));
    }
}
