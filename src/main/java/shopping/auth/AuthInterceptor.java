package shopping.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider provider;

    public AuthInterceptor(JwtTokenProvider provider) {
        this.provider = provider;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) {
        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            response.setStatus(401);
            log.warn("로그인 정보가 없습니다.");
            return false;
        }

        String token = authorization.substring(7);

        try {
            Long memberId = provider.extract(token);
            request.setAttribute("memberId", memberId);
            return true;
        } catch (IllegalArgumentException e) {
            response.setStatus(401);
            log.warn(e.getMessage());
            return false;
        }
    }
}
