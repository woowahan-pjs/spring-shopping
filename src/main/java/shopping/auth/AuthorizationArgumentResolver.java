package shopping.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Arrays;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class AuthorizationArgumentResolver implements HandlerMethodArgumentResolver {

    public static final String BEARER = "Bearer ";

    private final AccessTokenRepository accessTokenRepository;

    public AuthorizationArgumentResolver(final AccessTokenRepository accessTokenRepository) {
        this.accessTokenRepository = accessTokenRepository;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Authorization.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter,
                                  final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest,
                                  final WebDataBinderFactory binderFactory) throws Exception {
        final Authorization authorization = parameter.getParameterAnnotation(Authorization.class);
        final HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        final String authorizationHeader = request.getHeader(AUTHORIZATION);
        final String accessToken = parsingBearerToken(authorizationHeader);
        final AuthorizationUser authorizationUser = Arrays.stream(authorization.value())
                .filter(it -> accessTokenRepository.exists(it, accessToken))
                .map(it -> accessTokenRepository.find(it, accessToken))
                .map(AuthorizationUser::new)
                .findFirst()
                .orElseThrow(() -> new BadRequestException());
        return authorizationUser;
    }

    private String parsingBearerToken(final String bearerToken) {
        if (bearerToken.startsWith(BEARER)) {
            return bearerToken.substring(BEARER.length());
        }
        return bearerToken;
    }
}
