package shopping.auth.adapter.in.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import shopping.auth.adapter.in.web.access.AccessPolicyResolver;
import shopping.auth.adapter.in.web.access.AccessType;
import shopping.auth.service.AuthService;
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
        if (!isControllerHandler(handler)) {
            return true;
        }

        HandlerMethod controllerHandler = (HandlerMethod) handler;
        AccessType accessType = accessPolicyResolver.resolve(controllerHandler);
        if (accessType.isPublic()) {
            return true;
        }

        Long authenticatedMemberId = resolveAuthenticatedMemberId(request);
        storeAuthenticatedMemberId(request, authenticatedMemberId);
        validateSellerAccessIfRequired(accessType, authenticatedMemberId);
        return true;
    }

    private boolean isControllerHandler(Object handler) {
        return handler instanceof HandlerMethod;
    }

    private Long resolveAuthenticatedMemberId(HttpServletRequest request) {
        Long storedMemberId = findStoredMemberId(request);
        if (storedMemberId != null) {
            return storedMemberId;
        }

        return authenticateWithAuthorizationHeader(request);
    }

    private Long findStoredMemberId(HttpServletRequest request) {
        Object storedMemberId = request.getAttribute(AuthAttributes.MEMBER_ID_ATTRIBUTE);
        if (storedMemberId instanceof Long memberId) {
            return memberId;
        }
        return null;
    }

    private Long authenticateWithAuthorizationHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        return authService.authenticate(authorizationHeader);
    }

    private void storeAuthenticatedMemberId(HttpServletRequest request, Long authenticatedMemberId) {
        request.setAttribute(AuthAttributes.MEMBER_ID_ATTRIBUTE, authenticatedMemberId);
    }

    private void validateSellerAccessIfRequired(AccessType accessType, Long authenticatedMemberId) {
        if (!accessType.requiresSellerRole()) {
            return;
        }

        memberService.requireActiveSeller(authenticatedMemberId);
    }
}
