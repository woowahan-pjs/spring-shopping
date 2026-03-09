package shopping.auth.adapter.in.web;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import shopping.auth.AuthProperties;

@DisplayName("[인증] 리프레시 토큰 쿠키 관리자 단위 테스트")
class RefreshTokenCookieManagerTest {
    @Test
    @DisplayName("refresh token을 HttpOnly 쿠키로 내려준다")
    void writeSetRefreshTokenCookie() {
        RefreshTokenCookieManager refreshTokenCookieManager = new RefreshTokenCookieManager(authProperties());
        MockHttpServletResponse response = new MockHttpServletResponse();

        refreshTokenCookieManager.write(response, "refresh-token-value");

        String setCookie = response.getHeader("Set-Cookie");
        assertThat(setCookie).contains("refreshToken=refresh-token-value");
        assertThat(setCookie).contains("HttpOnly");
        assertThat(setCookie).contains("Path=/api/auth/refresh");
    }

    @Test
    @DisplayName("요청 쿠키에서 refresh token을 찾는다")
    void resolveReturnRefreshTokenFromCookie() {
        RefreshTokenCookieManager refreshTokenCookieManager = new RefreshTokenCookieManager(authProperties());
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("refreshToken", "refresh-token-value"));

        String refreshToken = refreshTokenCookieManager.resolve(request);

        assertThat(refreshToken).isEqualTo("refresh-token-value");
    }

    @Test
    @DisplayName("쿠키가 없으면 refresh token이 없다고 본다")
    void resolveReturnNullWhenCookiesAreMissing() {
        RefreshTokenCookieManager refreshTokenCookieManager = new RefreshTokenCookieManager(authProperties());
        MockHttpServletRequest request = new MockHttpServletRequest();

        String refreshToken = refreshTokenCookieManager.resolve(request);

        assertThat(refreshToken).isNull();
    }

    private AuthProperties authProperties() {
        AuthProperties authProperties = new AuthProperties();
        authProperties.setRefreshTokenCookieName("refreshToken");
        authProperties.setRefreshTokenValidityDays(7L);
        authProperties.setRefreshTokenCookieSecure(false);
        return authProperties;
    }
}
