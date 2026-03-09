package shopping.common.monitoring;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.LongSupplier;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import shopping.auth.adapter.in.web.AuthAttributes;
import shopping.common.ApiException;
import shopping.common.ErrorCode;

class ApiAuditLoggingAspectTest {
    private final Logger logger = (Logger) LoggerFactory.getLogger(ApiAuditLoggingAspect.class);
    private final ListAppender<ILoggingEvent> listAppender = new ListAppender<>();

    @AfterEach
    void tearDown() {
        logger.detachAppender(listAppender);
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    @DisplayName("정상 API 호출은 info 감사 로그를 남긴다")
    void logInfoWhenApiCallSucceedsWithinThreshold() throws Throwable {
        // given
        attachAppender();
        MockHttpServletRequest request = request("POST", "/api/members/login");
        request.addHeader("Authorization", "Bearer secret-token");
        request.addHeader("Cookie", "refreshToken=secret-cookie");
        request.setAttribute(AuthAttributes.MEMBER_ID_ATTRIBUTE, 7L);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        ApiAuditLoggingAspect aspect = new ApiAuditLoggingAspect(nanoTimeSupplier(0L, 500_000_000L));
        ProceedingJoinPoint joinPoint = joinPoint(ResponseEntity.status(HttpStatus.CREATED).build());

        // when
        Object result = aspect.logApiCall(joinPoint);

        // then
        assertThat(result).isEqualTo(ResponseEntity.status(HttpStatus.CREATED).build());
        ILoggingEvent event = onlyEvent();
        assertThat(event.getLevel()).isEqualTo(Level.INFO);
        assertThat(event.getFormattedMessage())
                .contains("method=POST")
                .contains("path=/api/members/login")
                .contains("controller=StubController")
                .contains("memberId=7")
                .contains("status=201")
                .contains("durationMs=500")
                .contains("outcome=SUCCESS")
                .doesNotContain("secret-token")
                .doesNotContain("secret-cookie");
    }

    @Test
    @DisplayName("실행시간이 1000ms 이상이면 warn 로그를 남긴다")
    void logWarnWhenApiCallIsSlow() throws Throwable {
        // given
        attachAppender();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request("GET", "/api/products")));
        ApiAuditLoggingAspect aspect = new ApiAuditLoggingAspect(nanoTimeSupplier(0L, 1_250_000_000L));
        ProceedingJoinPoint joinPoint = joinPoint(ResponseEntity.ok().build());

        // when
        aspect.logApiCall(joinPoint);

        // then
        ILoggingEvent event = onlyEvent();
        assertThat(event.getLevel()).isEqualTo(Level.WARN);
        assertThat(event.getFormattedMessage())
                .contains("status=200")
                .contains("durationMs=1250")
                .contains("outcome=SUCCESS");
    }

    @Test
    @DisplayName("ApiException이 발생하면 status와 errorCode를 담아 warn 로그를 남긴다")
    void logWarnWhenApiExceptionOccurs() throws Throwable {
        // given
        attachAppender();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request("DELETE", "/api/products/1")));
        ApiAuditLoggingAspect aspect = new ApiAuditLoggingAspect(nanoTimeSupplier(0L, 200_000_000L));
        ProceedingJoinPoint joinPoint = joinPoint(new ApiException(ErrorCode.PRODUCT_OWNER_FORBIDDEN));

        // when
        // then
        assertThatThrownBy(() -> aspect.logApiCall(joinPoint))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.PRODUCT_OWNER_FORBIDDEN);

        ILoggingEvent event = onlyEvent();
        assertThat(event.getLevel()).isEqualTo(Level.WARN);
        assertThat(event.getThrowableProxy()).isNull();
        assertThat(event.getFormattedMessage())
                .contains("method=DELETE")
                .contains("path=/api/products/1")
                .contains("status=403")
                .contains("durationMs=200")
                .contains("outcome=FAILURE")
                .contains("errorCode=PRODUCT_OWNER_FORBIDDEN");
    }

    @Test
    @DisplayName("일반 예외가 발생하면 errorCode 없이 warn 로그를 남긴다")
    void logWarnWithoutErrorCodeWhenUnexpectedExceptionOccurs() throws Throwable {
        // given
        attachAppender();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request("GET", "/api/products")));
        ApiAuditLoggingAspect aspect = new ApiAuditLoggingAspect(nanoTimeSupplier(0L, 300_000_000L));
        ProceedingJoinPoint joinPoint = joinPoint(new IllegalStateException("boom"));

        // when
        // then
        assertThatThrownBy(() -> aspect.logApiCall(joinPoint))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("boom");

        ILoggingEvent event = onlyEvent();
        assertThat(event.getLevel()).isEqualTo(Level.WARN);
        assertThat(event.getFormattedMessage())
                .contains("status=500")
                .contains("durationMs=300")
                .contains("outcome=FAILURE")
                .doesNotContain("errorCode=");
    }

    @Test
    @DisplayName("요청 헤더와 쿠키 값은 감사 로그에 남기지 않는다")
    void doNotLogSensitiveHeaderOrCookieValues() throws Throwable {
        // given
        attachAppender();
        MockHttpServletRequest request = request("POST", "/api/auth/refresh");
        request.addHeader("Authorization", "Bearer top-secret-token");
        request.addHeader("Cookie", "refreshToken=top-secret-cookie");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        ApiAuditLoggingAspect aspect = new ApiAuditLoggingAspect(nanoTimeSupplier(0L, 100_000_000L));
        ProceedingJoinPoint joinPoint = joinPoint(ResponseEntity.ok().build());

        // when
        aspect.logApiCall(joinPoint);

        // then
        ILoggingEvent event = onlyEvent();
        assertThat(event.getFormattedMessage())
                .doesNotContain("top-secret-token")
                .doesNotContain("top-secret-cookie")
                .doesNotContain("Authorization")
                .doesNotContain("Cookie");
    }

    private void attachAppender() {
        listAppender.start();
        logger.addAppender(listAppender);
    }

    private ILoggingEvent onlyEvent() {
        assertThat(listAppender.list).hasSize(1);
        return listAppender.list.get(0);
    }

    private MockHttpServletRequest request(String method, String requestUri) {
        MockHttpServletRequest request = new MockHttpServletRequest(method, requestUri);
        request.setRequestURI(requestUri);
        return request;
    }

    private ProceedingJoinPoint joinPoint(Object result) throws Throwable {
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        Signature signature = mock(Signature.class);
        when(signature.getDeclaringType()).thenReturn(StubController.class);
        when(joinPoint.getSignature()).thenReturn(signature);
        if (result instanceof Throwable throwable) {
            when(joinPoint.proceed()).thenThrow(throwable);
        } else {
            when(joinPoint.proceed()).thenReturn(result);
        }
        return joinPoint;
    }

    private LongSupplier nanoTimeSupplier(long... values) {
        AtomicInteger index = new AtomicInteger();
        return () -> values[index.getAndIncrement()];
    }

    static class StubController {
    }
}
