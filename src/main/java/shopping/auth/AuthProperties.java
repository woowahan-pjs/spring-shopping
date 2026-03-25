package shopping.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.auth")
public record AuthProperties(
        String jwtSecret,
        long tokenValidityMinutes,
        long refreshTokenValidityDays,
        String refreshTokenCookieName,
        boolean refreshTokenCookieSecure
) {
}
