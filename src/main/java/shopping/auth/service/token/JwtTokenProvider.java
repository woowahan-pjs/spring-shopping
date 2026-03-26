package shopping.auth.service.token;

import io.jsonwebtoken.Jwts;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider implements TokenProvider {
    private final JwtProperties jwtProperties;

    @Override
    public String createToken(Long memberId) {
        return Jwts.builder()
                   .subject(String.valueOf(memberId))
                   .expiration(new Date(System.currentTimeMillis() + jwtProperties.expirationMs()))
                   .signWith(jwtProperties.secretKey())
                   .compact();
    }

    @Override
    public Long getMemberId(String token) {
        String subject = Jwts.parser()
                             .verifyWith(jwtProperties.secretKey())
                             .build()
                             .parseSignedClaims(token)
                             .getPayload()
                             .getSubject();
        return Long.parseLong(subject);
    }
}
