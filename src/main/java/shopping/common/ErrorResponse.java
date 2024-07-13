package shopping.common;

import java.util.Collections;
import java.util.List;
import org.springframework.validation.BindingResult;

public record ErrorResponse(String message, List<FieldError> errors) {
    public static ErrorResponse from(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getMessage(), Collections.emptyList());
    }

    public static ErrorResponse from(ErrorCode errorCode, BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors()
                .stream()
                .map(fieldError -> {
                    String field = fieldError.getField();
                    String rejectedValue = (String) fieldError.getRejectedValue();
                    String reason = fieldError.getDefaultMessage();
                    return new FieldError(field, rejectedValue, reason);
                })
                .toList();
        return new ErrorResponse(errorCode.getMessage(), fieldErrors);
    }

    private record FieldError(String field, String value, String reason) {
    }
}