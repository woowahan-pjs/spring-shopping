package shopping.common.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import shopping.common.exception.ApiException;
import shopping.common.exception.ErrorType;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final TokenProvider tokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = extractToken(request);
        Long memberId = tokenProvider.extractMemberId(token);
        String role = tokenProvider.extractRole(token);
        request.setAttribute("memberId", memberId);
        request.setAttribute("role", role);
        return true;
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION_HEADER);
        if (!StringUtils.hasText(header) || !header.startsWith(BEARER_PREFIX)) {
            throw new ApiException("토큰이 없습니다", ErrorType.INVALID_TOKEN, HttpStatus.UNAUTHORIZED);
        }
        return header.substring(BEARER_PREFIX.length());
    }
}
