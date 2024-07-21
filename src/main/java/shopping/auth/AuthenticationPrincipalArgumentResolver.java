package shopping.auth;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import shopping.token.application.TokenService;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    private final TokenService tokenService;

    public AuthenticationPrincipalArgumentResolver(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String authorizationHeader = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        validateHeader(authorizationHeader);

        String email = tokenService.extractEmail(authorizationHeader);
        return new UserDetails(email);
    }

    private void validateHeader(String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            throw new RequiresTokenException();
        }
    }
}
