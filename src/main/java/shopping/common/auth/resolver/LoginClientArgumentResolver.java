package shopping.common.auth.resolver;

import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import shopping.common.auth.exception.UnAuthenticatedException;
import shopping.member.client.domain.Client;
import shopping.member.common.application.AuthService;
import shopping.member.common.domain.Member;
import shopping.member.common.domain.MemberRole;

@Component
@RequiredArgsConstructor
public class LoginClientArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthService authService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginClient.class)
                && parameter.getParameterType().equals(Client.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        try {
            return getClient();
        } catch (RuntimeException e) {
            throw new UnAuthenticatedException("인증에 실패하였습니다.");
        }
    }

    private Member getClient() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return authService.findMember(userDetails.getUsername(), MemberRole.CLIENT);
    }
}
