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
        AuditContext context = auditContext(joinPoint);

        try {
            Object result = joinPoint.proceed();
            writeLog(AuditLog.success(context, responseStatus(result), elapsedMillis(startedAt)));
            return result;
        } catch (Throwable throwable) {
            writeLog(AuditLog.failure(context, failureStatus(throwable), elapsedMillis(startedAt), errorCode(throwable)));
            throw throwable;
        }
    }

    private void writeLog(AuditLog auditLog) {
        if (shouldWarn(auditLog)) {
            log.warn(auditLog.format(), auditLog.arguments());
            return;
        }

        log.info(auditLog.format(), auditLog.arguments());
    }

    private boolean shouldWarn(AuditLog auditLog) {
        return auditLog.isFailure() || auditLog.durationMs() >= WARN_THRESHOLD_MS;
    }

    private AuditContext auditContext(ProceedingJoinPoint joinPoint) {
        HttpServletRequest request = currentRequest();
        return new AuditContext(
                requestMethod(request),
                requestPath(request),
                controllerName(joinPoint),
                memberId(request)
        );
    }

    private HttpServletRequest currentRequest() {
        if (!(RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes)) {
            return null;
        }
        return attributes.getRequest();
    }

    private String requestMethod(HttpServletRequest request) {
        if (request == null) {
            return UNKNOWN;
        }
        return request.getMethod();
    }

    private String requestPath(HttpServletRequest request) {
        if (request == null) {
            return UNKNOWN;
        }
        return request.getRequestURI();
    }

    private Long memberId(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        Object authenticatedMemberId = request.getAttribute(AuthAttributes.MEMBER_ID_ATTRIBUTE);
        if (authenticatedMemberId instanceof Long value) {
            return value;
        }
        return null;
    }

    private String controllerName(ProceedingJoinPoint joinPoint) {
        return joinPoint.getSignature().getDeclaringType().getSimpleName();
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

    private enum Outcome {
        SUCCESS,
        FAILURE
    }

    private record AuditContext(String method, String path, String controller, Long memberId) {
    }

    private record AuditLog(AuditContext context, int status, long durationMs, Outcome outcome, String errorCode) {
        private static AuditLog success(AuditContext context, int status, long durationMs) {
            return new AuditLog(context, status, durationMs, Outcome.SUCCESS, null);
        }

        private static AuditLog failure(AuditContext context, int status, long durationMs, String errorCode) {
            return new AuditLog(context, status, durationMs, Outcome.FAILURE, errorCode);
        }

        private boolean isFailure() {
            return outcome == Outcome.FAILURE;
        }

        private String format() {
            if (errorCode == null) {
                return AUDIT_LOG_FORMAT;
            }
            return AUDIT_LOG_WITH_ERROR_CODE_FORMAT;
        }

        private Object[] arguments() {
            if (errorCode == null) {
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
    }
}
