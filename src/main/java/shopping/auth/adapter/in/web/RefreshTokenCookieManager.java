package shopping.auth.adapter.in.web;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import shopping.auth.AuthProperties;

@Component
public class RefreshTokenCookieManager {
    private static final String REFRESH_TOKEN_PATH = "/api/auth/refresh";

    private final String cookieName;
    private final long refreshTokenValidityDays;
    private final boolean secure;

    public RefreshTokenCookieManager(AuthProperties authProperties) {
        this.cookieName = authProperties.refreshTokenCookieName();
        this.refreshTokenValidityDays = authProperties.refreshTokenValidityDays();
        this.secure = authProperties.refreshTokenCookieSecure();
    }

    public void write(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from(cookieName, refreshToken)
                .httpOnly(true)
                .secure(secure)
                .sameSite("Strict")
                .path(REFRESH_TOKEN_PATH)
                .maxAge(Duration.ofDays(refreshTokenValidityDays))
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public String resolve(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
