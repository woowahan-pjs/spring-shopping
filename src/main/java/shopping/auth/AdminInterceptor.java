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

        String token = request.getHeader("Authorization")
                .substring(7);

        try {
            MemberRole role = provider.extractRole(token);
            if (role != MemberRole.ADMIN) {
                response.sendError(403);
                return false;
            }
        } catch (IllegalArgumentException e) {
            response.setStatus(401);
            log.warn(e.getMessage());
            return false;
        }

        return true;
    }
}
