package shopping.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Exception. message={}, className={}", e.getMessage(), e.getClass().getName());
        return ResponseEntity
            .internalServerError()
            .body(new ErrorResponse(e.getMessage(), ErrorType.UNKNOWN_ERROR));
    }
}
