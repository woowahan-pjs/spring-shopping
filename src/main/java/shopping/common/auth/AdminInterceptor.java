package shopping.common.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import shopping.common.exception.ApiException;
import shopping.common.exception.ErrorType;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        RoleRequired roleRequired = handlerMethod.getMethodAnnotation(RoleRequired.class);
        if (roleRequired == null) {
            return true;
        }

        // 필요권한
        AuthRole requiredRole = roleRequired.value();
        // 유저의 권한
        AuthRole userRole = AuthRole.from((String) request.getAttribute("role"));

        if (!requiredRole.isAccessible(userRole)) {
            throw new ApiException(AuthErrorMessage.NOT_AUTHORIZED.getDescription(),
                ErrorType.FORBIDDEN, HttpStatus.FORBIDDEN);
        }

        return true;
    }
}
