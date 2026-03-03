package shopping.infra.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Getter
@Configuration
class JwtTokenProperties {

    private final String secretKey;

    private final Long expiration;

    public JwtTokenProperties(
            @Value("${jwt.secret}") final String secretKey,
            @Value("${jwt.expiration}") final Long expiration) {
        this.secretKey = secretKey;
        this.expiration = expiration;
    }
}
