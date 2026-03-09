package shopping.common;

import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException exception) {
        if (exception.getStatus().is5xxServerError()) {
            log.error(
                    "ApiException occurred. code={}, status={}, message={}",
                    exception.getCode(),
                    exception.getStatus().value(),
                    exception.getMessage(),
                    exception
            );
        }
        return errorResponse(exception.getErrorCode(), exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toMessage)
                .collect(Collectors.joining(", "));
        if (message.isBlank()) {
            return errorResponse(ErrorCode.INVALID_INPUT);
        }
        return errorResponse(ErrorCode.INVALID_INPUT, message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidBody(HttpMessageNotReadableException exception) {
        return errorResponse(ErrorCode.INVALID_INPUT);
    }

    @ExceptionHandler({NoResourceFoundException.class, NoHandlerFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFound(Exception exception) {
        return errorResponse(ErrorCode.RESOURCE_NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception exception) {
        log.error("Unexpected exception occurred.", exception);
        return errorResponse(ErrorCode.INTERNAL_ERROR);
    }

    private String toMessage(FieldError error) {
        return error.getField() + ": " + error.getDefaultMessage();
    }

    private ResponseEntity<ErrorResponse> errorResponse(ErrorCode errorCode) {
        return errorResponse(errorCode, errorCode.message());
    }

    private ResponseEntity<ErrorResponse> errorResponse(ErrorCode errorCode, String message) {
        ErrorResponse response = new ErrorResponse(errorCode.code(), message);
        return ResponseEntity.status(errorCode.status()).body(response);
    }
}
