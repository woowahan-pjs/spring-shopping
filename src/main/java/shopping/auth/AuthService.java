package shopping.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Service
public class AuthService {
    private final SecretKey secretKey;

    public AuthService(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(Long memberId) {
        return Jwts.builder()
                   .subject(String.valueOf(memberId))
                   .signWith(secretKey)
                   .compact();
    }

    public Long getMemberId(String token) {
        String subject = Jwts.parser()
                             .verifyWith(secretKey)
                             .build()
                             .parseSignedClaims(token)
                             .getPayload()
                             .getSubject();
        return Long.parseLong(subject);
    }
}
