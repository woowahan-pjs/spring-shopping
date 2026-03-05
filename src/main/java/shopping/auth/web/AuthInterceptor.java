package shopping.auth.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import shopping.auth.access.AccessPolicyResolver;
import shopping.auth.access.AccessType;
import shopping.auth.application.AuthService;
import shopping.member.service.MemberService;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final AuthService authService;
    private final MemberService memberService;
    private final AccessPolicyResolver accessPolicyResolver;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }
        AccessType accessType = accessPolicyResolver.resolve(handlerMethod);
        if (accessType == AccessType.PUBLIC) {
            return true;
        }

        Long memberId = authenticate(request);
        request.setAttribute(AuthAttributes.MEMBER_ID_ATTRIBUTE, memberId);

        if (accessType == AccessType.SELLER) {
            memberService.requireActiveSeller(memberId);
        }
        return true;
    }

    private Long authenticate(HttpServletRequest request) {
        Object existing = request.getAttribute(AuthAttributes.MEMBER_ID_ATTRIBUTE);
        if (existing instanceof Long memberId) {
            return memberId;
        }
        String authorization = request.getHeader(AUTHORIZATION_HEADER);
        return authService.authenticate(authorization);
    }
}
