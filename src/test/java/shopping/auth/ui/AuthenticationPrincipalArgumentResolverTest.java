package shopping.auth.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import shopping.auth.application.JwtTokenProvider;
import shopping.auth.application.dto.TokenInfo;
import shopping.auth.exception.AuthenticationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthenticationPrincipalArgumentResolverTest {

    private JwtTokenProvider jwtTokenProvider;
    private AuthenticationPrincipalArgumentResolver resolver;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = mock(JwtTokenProvider.class);
        resolver = new AuthenticationPrincipalArgumentResolver(jwtTokenProvider);
    }

    @Test
    @DisplayName("토큰이 포함된 요청에서 UserPrincipal 를 추출할 수 있다")
    void resolveArgumentWithValidToken() throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer validToken");
        final TokenInfo tokenInfo = new TokenInfo(1L, "test@test.com");
        final NativeWebRequest webRequest = new ServletWebRequest(request);
        final MethodParameter methodParameter = mock(MethodParameter.class);

        when(jwtTokenProvider.getTokenInfo(anyString())).thenReturn(tokenInfo);

        final Object result = resolver.resolveArgument(methodParameter, null, webRequest, null);

        assertThat(result).isEqualTo(new UserPrincipal(1L, "test@test.com"));
    }

    @Test
    @DisplayName("토큰이 없는 경우 예외 발생가 발생한다.")
    void resolveArgumentWithoutToken() {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        final NativeWebRequest webRequest = new ServletWebRequest(request);
        final MethodParameter methodParameter = mock(MethodParameter.class);

        assertThatThrownBy(() -> resolver.resolveArgument(methodParameter, null, webRequest, null))
                .isInstanceOf(AuthenticationException.class);
    }

    @Test
    @DisplayName("토큰이 없지만 인증이 필요하지 않은 경우 빈 객체를 반환한다.")
    void resolveArgumentWithoutTokenAndNotRequired() throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        final NativeWebRequest webRequest = new ServletWebRequest(request);
        final MethodParameter methodParameter = mock(MethodParameter.class);
        final AuthenticationPrincipal annotation = mock(AuthenticationPrincipal.class);

        when(methodParameter.getParameterAnnotation(AuthenticationPrincipal.class)).thenReturn(annotation);
        when(annotation.required()).thenReturn(false);

        final Object result = resolver.resolveArgument(methodParameter, null, webRequest, null);

        assertThat(result).isEqualTo(UserPrincipal.empty());
    }
}
