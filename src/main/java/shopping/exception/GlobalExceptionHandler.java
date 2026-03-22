package shopping.exception;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<List<String>> handleValidationException(MethodArgumentNotValidException e) {
		List<String> errors = e.getBindingResult().getFieldErrors().stream()
			.map(fieldError -> {
				String field = fieldError.getField();
				Object rejectedValue = fieldError.getRejectedValue();
				String message = fieldError.getDefaultMessage();
				log.warn("유효성 검증 실패 - 필드: {}, 잘못 입력된 값: {}, 실패 사유: {}", field, rejectedValue, message);
				return "[" + field + "] " + message;
			})
			.toList();

		return ResponseEntity.badRequest().body(errors);
	}
}