package shopping.auth.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import shopping.common.ApiException;
import shopping.common.ErrorCode;

@Component
public class JwtTokenProvider {
    private final SecretKey signingKey;
    private final long tokenValidityMinutes;

    public JwtTokenProvider(
            @Value("${app.auth.jwt-secret}") String secret,
            @Value("${app.auth.token-validity-minutes}") long tokenValidityMinutes
    ) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.tokenValidityMinutes = tokenValidityMinutes;
    }

    public String create(Long memberId) {
        LocalDateTime now = LocalDateTime.now();
        Date issuedAt = toDate(now);
        Date expiration = toDate(now.plusMinutes(tokenValidityMinutes));
        return Jwts.builder()
                .subject(String.valueOf(memberId))
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(signingKey)
                .compact();
    }

    public JwtSubject parse(String token) {
        Claims claims = parseClaims(token);
        try {
            Long memberId = Long.parseLong(claims.getSubject());
            return new JwtSubject(memberId);
        } catch (NumberFormatException exception) {
            throw new ApiException(ErrorCode.AUTHENTICATION_TOKEN_INVALID);
        }
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser().verifyWith(signingKey).build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception exception) {
            throw new ApiException(ErrorCode.AUTHENTICATION_TOKEN_INVALID);
        }
    }

    private Date toDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
