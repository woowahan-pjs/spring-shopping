package shopping.token.domain;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenProvider {
    private static final String TOKEN_SUBJECT = "AccessToken";
    private static final String EMAIL_KEY = "email";
    private static final String BEARER = "Bearer ";

    private final SecretKey secretKey;
    private final long tokenExpirationSeconds;

    public TokenProvider(@Value("${jwt.secret}") String secret, @Value("${jwt.access.expiration}") long tokenExpirationSeconds) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.tokenExpirationSeconds = tokenExpirationSeconds;
    }

    public Token generate(String email) {
        Date expiredAt = new Date(System.currentTimeMillis() + tokenExpirationSeconds * 1000L);
        String jwtToken = Jwts.builder()
                .subject(TOKEN_SUBJECT)
                .expiration(expiredAt)
                .claim(EMAIL_KEY, email)
                .signWith(secretKey)
                .compact();
        return JwtToken.from(jwtToken);
    }

    public String extractEmail(String authorizationHeader) {
        validateAuthorizationHeader(authorizationHeader);

        String token = authorizationHeader.substring(BEARER.length());
        validateToken(token);

        Claims payload = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return payload.get(EMAIL_KEY, String.class);
    }

    private void validateAuthorizationHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER)) {
            throw new InvalidTokenException();
        }
    }

    private void validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
        } catch (Exception e) {
            throw new InvalidTokenException();
        }
    }
}
