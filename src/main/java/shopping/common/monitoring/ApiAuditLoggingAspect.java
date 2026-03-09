package shopping.common.monitoring;

import jakarta.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;
import java.util.function.LongSupplier;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import shopping.auth.adapter.in.web.AuthAttributes;
import shopping.common.ApiException;

@Slf4j
@Aspect
@Component
public class ApiAuditLoggingAspect {
    private static final long WARN_THRESHOLD_MS = 1_000L;
    private static final String UNKNOWN = "UNKNOWN";
    private static final String AUDIT_LOG_FORMAT =
            "api_audit method={} path={} controller={} memberId={} status={} durationMs={} outcome={}";
    private static final String AUDIT_LOG_WITH_ERROR_CODE_FORMAT = AUDIT_LOG_FORMAT + " errorCode={}";

    private final LongSupplier nanoTimeSupplier;

    public ApiAuditLoggingAspect() {
        this(System::nanoTime);
    }

    ApiAuditLoggingAspect(LongSupplier nanoTimeSupplier) {
        this.nanoTimeSupplier = nanoTimeSupplier;
    }

    @Around("@within(org.springframework.web.bind.annotation.RestController) || within(shopping..adapter.in..*Controller)")
    public Object logApiCall(ProceedingJoinPoint joinPoint) throws Throwable {
        long startedAt = nanoTimeSupplier.getAsLong();
        AuditContext context = AuditContext.from(joinPoint, currentRequest());

        try {
            Object result = joinPoint.proceed();
            logSuccess(context, responseStatus(result), elapsedMillis(startedAt));
            return result;
        } catch (Throwable throwable) {
            logFailure(context, throwable, elapsedMillis(startedAt));
            throw throwable;
        }
    }

    private void logSuccess(AuditContext context, int status, long durationMs) {
        Object[] arguments = auditArguments(context, status, durationMs, "SUCCESS");
        if (durationMs >= WARN_THRESHOLD_MS) {
            log.warn(AUDIT_LOG_FORMAT, arguments);
            return;
        }
        log.info(AUDIT_LOG_FORMAT, arguments);
    }

    private void logFailure(AuditContext context, Throwable throwable, long durationMs) {
        int status = failureStatus(throwable);
        String errorCode = errorCode(throwable);
        if (errorCode == null) {
            log.warn(AUDIT_LOG_FORMAT, auditArguments(context, status, durationMs, "FAILURE"));
            return;
        }
        log.warn(
                AUDIT_LOG_WITH_ERROR_CODE_FORMAT,
                auditArguments(context, status, durationMs, "FAILURE", errorCode)
        );
    }

    private HttpServletRequest currentRequest() {
        if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes) {
            return attributes.getRequest();
        }
        return null;
    }

    private long elapsedMillis(long startedAt) {
        return TimeUnit.NANOSECONDS.toMillis(nanoTimeSupplier.getAsLong() - startedAt);
    }

    private int responseStatus(Object result) {
        if (result instanceof ResponseEntity<?> responseEntity) {
            return responseEntity.getStatusCode().value();
        }
        return HttpStatus.OK.value();
    }

    private int failureStatus(Throwable throwable) {
        if (throwable instanceof ApiException apiException) {
            return apiException.getStatus().value();
        }
        return HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    private String errorCode(Throwable throwable) {
        if (throwable instanceof ApiException apiException) {
            return apiException.getCode();
        }
        return null;
    }

    private Object[] auditArguments(AuditContext context, int status, long durationMs, String outcome) {
        return new Object[]{
                context.method(),
                context.path(),
                context.controller(),
                context.memberId(),
                status,
                durationMs,
                outcome
        };
    }

    private Object[] auditArguments(AuditContext context, int status, long durationMs, String outcome, String errorCode) {
        return new Object[]{
                context.method(),
                context.path(),
                context.controller(),
                context.memberId(),
                status,
                durationMs,
                outcome,
                errorCode
        };
    }

    private record AuditContext(String method, String path, String controller, Long memberId) {
        private static AuditContext from(ProceedingJoinPoint joinPoint, HttpServletRequest request) {
            return new AuditContext(
                    request == null ? UNKNOWN : request.getMethod(),
                    request == null ? UNKNOWN : request.getRequestURI(),
                    joinPoint.getSignature().getDeclaringType().getSimpleName(),
                    memberId(request)
            );
        }

        private static Long memberId(HttpServletRequest request) {
            if (request == null) {
                return null;
            }

            Object authenticatedMemberId = request.getAttribute(AuthAttributes.MEMBER_ID_ATTRIBUTE);
            if (authenticatedMemberId instanceof Long value) {
                return value;
            }
            return null;
        }
    }
}
