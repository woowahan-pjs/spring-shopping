package shopping.auth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.auth")
public class AuthProperties {
    private String jwtSecret;
    private long tokenValidityMinutes;
    private long refreshTokenValidityDays;
    private String refreshTokenCookieName;
    private boolean refreshTokenCookieSecure;
}
