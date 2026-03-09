package shopping.infra.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import shopping.auth.domain.Role;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtTokenProperties jwtTokenProperties;

    public String generateToken(final UserPrincipal userPrincipal) {
        final Key key = getSigningKey(jwtTokenProperties.getSecretKey());

        return Jwts.builder()
                .header()
                .add("typ", "JWT")
                .and()
                .subject(userPrincipal.getUserIdToString())
                .claim("email", userPrincipal.getUsername())
                .claim("role", userPrincipal.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtTokenProperties.getExpiration()))
                .signWith(key)
                .compact();
    }

    public boolean isValidate(final String token) {
        try {
            parseClaims(jwtTokenProperties.getSecretKey(), token);

            return true;
        } catch (ExpiredJwtException e) {
            log.warn("토큰이 만료 되었습니다. : {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("지원하지 않는 토큰 형식입니다. : {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.warn("잘못된 토큰 형식입니다. : {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("토큰 입력이 잘못되었습니다. : {}", e.getMessage());
        } catch (Exception e) {
            log.warn("토큰 검증시 오류가 발생하였습니다. : {}", e.getMessage());
        }

        return false;
    }

    public Authentication getAuthentication(final String token) {
        final Claims claims = parseClaims(jwtTokenProperties.getSecretKey(), token);

        final Long id = Long.parseLong(claims.getSubject());
        final String email = claims.get("email", String.class);
        final Role userRole = Role.convert(claims.get("role", String.class));

        final UserDetails userDetails = UserPrincipal.generate(id, email, userRole);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private Claims parseClaims(final String secretKey, final String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey(secretKey))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey(final String key) {
        return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }
}
