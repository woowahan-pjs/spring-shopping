package shopping.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shopping.common.dto.ErrorResponse;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException e) {
        log.error("Api Exception occurred. message={}, className={}", e.getErrorMessage(),
            e.getClass().getName());
        return ResponseEntity
            .status(e.getHttpStatus())
            .body(new ErrorResponse(e.getErrorMessage(), e.getErrorType()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e) {
        String message = extractFirstErrorMessage(e);
        log.error("Validation Exception occurred. message={}", message);
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(message, ErrorType.INVALID_PARAMETER));
    }

    private String extractFirstErrorMessage(MethodArgumentNotValidException e) {
        return e.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .findFirst()
            .orElse("잘못된 요청입니다");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Exception. message={}, className={}", e.getMessage(), e.getClass().getName());
        return ResponseEntity
            .internalServerError()
            .body(new ErrorResponse(e.getMessage(), ErrorType.UNKNOWN_ERROR));
    }
}
