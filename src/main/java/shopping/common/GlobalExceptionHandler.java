package shopping.common;

import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
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
        return apiErrorResponse(exception.getErrorCode(), exception.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
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

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        return errorResponse(ErrorCode.INVALID_INPUT);
    }

    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(
            NoResourceFoundException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        return errorResponse(ErrorCode.RESOURCE_NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        return errorResponse(ErrorCode.RESOURCE_NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception exception) {
        log.error("Unexpected exception occurred.", exception);
        return apiErrorResponse(ErrorCode.INTERNAL_ERROR);
    }

    private String toMessage(FieldError error) {
        return error.getField() + ": " + error.getDefaultMessage();
    }

    private ResponseEntity<ErrorResponse> apiErrorResponse(ErrorCode errorCode) {
        return apiErrorResponse(errorCode, errorCode.message());
    }

    private ResponseEntity<ErrorResponse> apiErrorResponse(ErrorCode errorCode, String message) {
        ErrorResponse response = new ErrorResponse(errorCode.code(), message);
        return ResponseEntity.status(errorCode.status()).body(response);
    }

    private ResponseEntity<Object> errorResponse(ErrorCode errorCode) {
        return errorResponse(errorCode, errorCode.message());
    }

    private ResponseEntity<Object> errorResponse(ErrorCode errorCode, String message) {
        ErrorResponse response = new ErrorResponse(errorCode.code(), message);
        return ResponseEntity.status(errorCode.status()).body(response);
    }
}
