package shopping.auth;

import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(String secret, long expirationMs) {
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(secret().getBytes(StandardCharsets.UTF_8));
    }
}
