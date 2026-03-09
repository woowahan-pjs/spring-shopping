package shopping.auth.adapter.out;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;
import shopping.auth.AuthProperties;
import shopping.auth.domain.JwtSubject;
import shopping.common.ApiException;
import shopping.common.ErrorCode;

@Component
public class JwtTokenProvider {
    private final SecretKey signingKey;
    private final long tokenValidityMinutes;

    public JwtTokenProvider(AuthProperties authProperties) {
        this.signingKey = Keys.hmacShaKeyFor(authProperties.getJwtSecret().getBytes(StandardCharsets.UTF_8));
        this.tokenValidityMinutes = authProperties.getTokenValidityMinutes();
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
