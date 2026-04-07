package shopping.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import shopping.member.domain.Member;
import shopping.member.domain.MemberRole;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final SecretKey accessKey;
    private final SecretKey refreshKey;
    private final long accessExpirationMs;
    private final long refreshExpirationMs;

    public JwtTokenProvider(
            @Value("${jwt.access-secret}") String accessSecret,
            @Value("${jwt.refresh-secret}") String refreshSecret,
            @Value("${jwt.access-expiration-ms}") long accessExpirationMs,
            @Value("${jwt.refresh-expiration-ms}") long refreshExpirationMs) {
        this.accessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessSecret));
        this.refreshKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshSecret));
        this.accessExpirationMs = accessExpirationMs;
        this.refreshExpirationMs = refreshExpirationMs;
    }

    public String generateAccessToken(Member member) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + accessExpirationMs);

        return Jwts.builder()
                .subject(member.getId().toString())
                .claim("role", member.getRole().name())
                .issuedAt(now)
                .expiration(validity)
                .signWith(accessKey)
                .compact();
    }

    public String generateRefreshToken(Member member) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshExpirationMs);

        return Jwts.builder()
                .subject(member.getId().toString())
                .issuedAt(now)
                .expiration(validity)
                .signWith(refreshKey)
                .compact();
    }

    public Long extract(String token) {
        try {
            String subject = parseClaims(token, accessKey)
                    .getSubject();

            return Long.parseLong(subject);
        } catch (ExpiredJwtException e) {
            throw new IllegalArgumentException("만료된 토큰입니다.");
        } catch (JwtException e) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
    }

    public MemberRole extractRole(String token) {
        try {
            String role = parseClaims(token, accessKey)
                    .get("role", String.class);

            return MemberRole.valueOf(role);
        } catch (ExpiredJwtException e) {
            throw new IllegalArgumentException("만료된 토큰입니다.");
        } catch (JwtException e) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
    }

    public Long extractMemberIdFromRefreshToken(String token) {
        try {
            String subject = parseClaims(token, refreshKey)
                    .getSubject();

            return Long.parseLong(subject);
        } catch (ExpiredJwtException e) {
            throw new IllegalArgumentException("만료된 토큰입니다.");
        } catch (JwtException e) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
    }

    private Claims parseClaims(String token, SecretKey key) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
