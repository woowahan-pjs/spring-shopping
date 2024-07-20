package shopping.token.application;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import shopping.token.domain.JwtToken;
import shopping.token.domain.Token;

@Service
public class TokenService {
    private static final String TOKEN_SUBJECT = "AccessToken";
    private static final String USERNAME_CLAIM = "email";

    private final SecretKey secretKey;
    private final long tokenExpirationSeconds;

    public TokenService(@Value("${jwt.secret}") String secret, @Value("${jwt.access.expiration}") long tokenExpirationSeconds) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.tokenExpirationSeconds = tokenExpirationSeconds;
    }

    public Token generate(String email) {
        Date expiredAt = new Date(System.currentTimeMillis() + tokenExpirationSeconds * 1000L);
        String jwtToken = Jwts.builder()
                .subject(TOKEN_SUBJECT)
                .expiration(expiredAt)
                .claim(USERNAME_CLAIM, email)
                .signWith(secretKey)
                .compact();
        return JwtToken.from(jwtToken);
    }
}
