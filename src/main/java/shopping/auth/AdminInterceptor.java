package shopping.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import shopping.member.domain.MemberRole;

import java.io.IOException;

@Component
@Slf4j
public class AdminInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider provider;

    public AdminInterceptor(JwtTokenProvider provider) {
        this.provider = provider;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws IOException {
        if (request.getMethod().equals("GET")) {
            return true;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            log.warn("로그인 정보가 없습니다.");
            return false;
        }

        try {
            MemberRole role = provider.extractRole(authHeader.substring(7));
            if (role != MemberRole.ADMIN) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                log.warn("관리자 권한이 없습니다.");
                return false;
            }
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            log.warn(e.getMessage());
            return false;
        }

        return true;
    }
}
