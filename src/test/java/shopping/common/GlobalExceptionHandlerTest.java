package shopping.common;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@DisplayName("[공통] 전역 예외 처리 단위 테스트")
class GlobalExceptionHandlerTest {
    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    @DisplayName("ApiException은 상태 코드와 메시지를 응답으로 만든다")
    void handleApiExceptionReturnErrorResponse() {
        // given
        ApiException exception = new ApiException(ErrorCode.MEMBER_EMAIL_DUPLICATE, "중복된 이메일");

        // when
        ResponseEntity<ErrorResponse> response =
                handler.handleApiException(exception);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isEqualTo(new ErrorResponse("MEMBER_EMAIL_DUPLICATE", "중복된 이메일"));
    }

    @Test
    @DisplayName("검증 예외는 필드 에러 메시지를 합쳐서 응답한다")
    void handleValidationReturnJoinedMessage() throws Exception {
        // given
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "request");
        bindingResult.addError(new FieldError("request", "email", "must not be blank"));
        bindingResult.addError(new FieldError("request", "password", "must not be blank"));
        Method method = TestRequest.class.getMethod("request", String.class);
        MethodParameter parameter = new MethodParameter(method, 0);

        // when
        ResponseEntity<ErrorResponse> response =
                handler.handleValidation(new MethodArgumentNotValidException(parameter, bindingResult));

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo(
                new ErrorResponse("INVALID_INPUT", "email: must not be blank, password: must not be blank")
        );
    }

    @Test
    @DisplayName("필드 에러가 없으면 기본 INVALID_INPUT 응답을 만든다")
    void handleValidationReturnDefaultMessageWhenFieldErrorsAreMissing() throws Exception {
        // given
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "request");
        Method method = TestRequest.class.getMethod("request", String.class);
        MethodParameter parameter = new MethodParameter(method, 0);

        // when
        ResponseEntity<ErrorResponse> response =
                handler.handleValidation(new MethodArgumentNotValidException(parameter, bindingResult));

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo(
                new ErrorResponse("INVALID_INPUT", ErrorCode.INVALID_INPUT.message())
        );
    }

    @Test
    @DisplayName("본문 파싱 예외는 INVALID_INPUT 응답을 만든다")
    void handleInvalidBodyReturnInvalidInput() {
        // given
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("invalid body");

        // when
        ResponseEntity<ErrorResponse> response =
                handler.handleInvalidBody(exception);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo(
                new ErrorResponse("INVALID_INPUT", ErrorCode.INVALID_INPUT.message())
        );
    }

    @Test
    @DisplayName("존재하지 않는 리소스 예외는 RESOURCE_NOT_FOUND 응답을 만든다")
    void handleNoResourceFoundReturnNotFound() {
        // given
        NoResourceFoundException exception = new NoResourceFoundException(HttpMethod.GET, "/actuator/metrics");

        // when
        ResponseEntity<ErrorResponse> response = handler.handleNotFound(exception);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo(
                new ErrorResponse("RESOURCE_NOT_FOUND", ErrorCode.RESOURCE_NOT_FOUND.message())
        );
    }

    @Test
    @DisplayName("핸들러가 없는 요청은 RESOURCE_NOT_FOUND 응답을 만든다")
    void handleNoHandlerFoundReturnNotFound() {
        // given
        NoHandlerFoundException exception = new NoHandlerFoundException("GET", "/missing", new HttpHeaders());

        // when
        ResponseEntity<ErrorResponse> response = handler.handleNotFound(exception);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo(
                new ErrorResponse("RESOURCE_NOT_FOUND", ErrorCode.RESOURCE_NOT_FOUND.message())
        );
    }

    @Test
    @DisplayName("예상하지 못한 예외는 INTERNAL_ERROR 응답을 만든다")
    void handleUnexpectedReturnInternalError() {
        // given
        RuntimeException exception = new RuntimeException("boom");

        // when
        ResponseEntity<ErrorResponse> response = handler.handleUnexpected(exception);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo(
                new ErrorResponse("INTERNAL_ERROR", ErrorCode.INTERNAL_ERROR.message())
        );
    }

    static class TestRequest {
        public void request(String value) {
        }
    }
}
