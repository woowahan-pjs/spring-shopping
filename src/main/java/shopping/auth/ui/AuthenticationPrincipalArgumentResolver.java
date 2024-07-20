package shopping.auth.ui;

import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import shopping.auth.application.JwtTokenProvider;
import shopping.auth.application.dto.TokenInfo;
import shopping.auth.exception.AuthenticationException;

import java.util.Optional;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    public static final String AUTH_PREFIX = "Bearer ";
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationPrincipalArgumentResolver(final JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer, final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) throws Exception {
        final String authorization = webRequest.getHeader("Authorization");

        if (StringUtils.hasLength(authorization) && authorization.startsWith(AUTH_PREFIX)) {
            final String token = authorization.substring(AUTH_PREFIX.length());
            final TokenInfo tokenInfo = jwtTokenProvider.getTokenInfo(token);
            return new UserPrincipal(tokenInfo.getId(), tokenInfo.getEmail());
        }

        if (isAuthRequired(parameter)) {
            throw new AuthenticationException();
        }

        return UserPrincipal.empty();
    }

    private boolean isAuthRequired(final MethodParameter parameter) {
        final AuthenticationPrincipal annotation = parameter.getParameterAnnotation(AuthenticationPrincipal.class);
        return Optional.ofNullable(annotation)
                .map(AuthenticationPrincipal::required)
                .orElse(true);
    }

}
