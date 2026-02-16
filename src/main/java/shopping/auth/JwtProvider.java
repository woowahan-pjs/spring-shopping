package shopping.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Provides JWT token creation and validation.
 *
 * @author brian.kim
 * @since 1.0
 */
@Component
public class JwtProvider {
    private final SecretKey key;
    private final long expiration;

    @Autowired
    public JwtProvider(
        @Value("${jwt.secret}") String secret,
        @Value("${jwt.expiration}") long expiration
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expiration = expiration;
    }

    /**
     * Extracts the email (subject) from a signed JWT token.
     *
     * @param token the JWT token string
     * @return the email stored in the token's subject claim
     */
    public String getEmail(String token) {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }

    /**
     * Creates a new signed JWT token for the given email.
     *
     * @param email the email to store as the token's subject
     * @return the compact JWT token string
     */
    public String createToken(String email) {
        final Date now = new Date();
        final Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
            .subject(email)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(key)
            .compact();
    }
}
