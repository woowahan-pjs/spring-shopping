package shopping.auth.application;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import shopping.auth.application.dto.TokenInfo;
import shopping.auth.exception.AuthenticationException;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private static final String EMAIL_KEY = "email";
    private final SecretKey secretKey;
    private final long validityInMilliseconds;

    public JwtTokenProvider(
            @Value("${security.jwt.token.secret-key}") final String secretKey,
            @Value("${security.jwt.token.expire-length}") final long validityInMilliseconds) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String createToken(final Long id, final String email) {
        final Date now = new Date();
        final Date expiration = new Date(now.getTime() + validityInMilliseconds);
        return Jwts.builder()
                .subject(String.valueOf(id))
                .claim(EMAIL_KEY, email)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(secretKey)
                .compact();
    }

    public TokenInfo getTokenInfo(final String token) {
        if (!StringUtils.hasText(token)) {
            throw new AuthenticationException();
        }

        try {
            final JwtParser jwtParser = Jwts.parser()
                    .verifyWith(secretKey)
                    .build();
            final Claims claims = jwtParser.parseSignedClaims(token).getPayload();
            return new TokenInfo(Long.parseLong(claims.getSubject()), claims.get(EMAIL_KEY, String.class));
        } catch (final JwtException e) {
            throw new AuthenticationException();
        }
    }

}

