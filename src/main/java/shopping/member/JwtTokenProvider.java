package shopping.member;

import shopping.member.domain.TokenProvider;

import java.util.Date;
import java.util.UUID;

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
    public String createToken(UUID memberId) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationMillis);
        return Jwts.builder().subject(memberId.toString()).issuedAt(now).expiration(expiration)
                .signWith(key).compact();
    }

    @Override
    public UUID extractMemberId(String token) {
        String subject = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload()
                .getSubject();
        return UUID.fromString(subject);
    }
}
