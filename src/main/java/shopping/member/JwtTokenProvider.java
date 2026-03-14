package shopping.member;

import shopping.member.domain.TokenProvider;

import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;

public class JwtTokenProvider implements TokenProvider {

    private final SecretKey key;
    private final long expirationMillis;

    public JwtTokenProvider(SecretKey key, long expirationMillis) {
        this.key = key;
        this.expirationMillis = expirationMillis;
    }

    @Override
    public String createToken(String email) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationMillis);
        return Jwts.builder().subject(email).issuedAt(now).expiration(expiration).signWith(key)
                .compact();
    }

    @Override
    public String extractEmail(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload()
                .getSubject();
    }
}
