package shopping.auth.token;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import org.junit.jupiter.api.Test;
import shopping.common.ApiException;
import shopping.common.ErrorCode;

class JwtTokenProviderTest {
    private static final String SECRET_KEY = "sJ55n8qtAW1mWL7v0ltWb6N7zseMqnPw5Y6x8tl1b2kvVDRj";

    @Test
    void createAndParseReturnSameMemberId() {
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(SECRET_KEY, 30);

        String token = jwtTokenProvider.create(99L);
        JwtSubject subject = jwtTokenProvider.parse(token);

        assertThat(subject.memberId()).isEqualTo(99L);
    }

    @Test
    void parseThrowWhenTokenIsTampered() {
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(SECRET_KEY, 30);

        String token = jwtTokenProvider.create(1L) + "tampered";

        assertThatThrownBy(() -> jwtTokenProvider.parse(token))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.AUTHENTICATION_TOKEN_INVALID);
    }

    @Test
    void parseThrowWhenTokenSignedWithDifferentSecret() {
        JwtTokenProvider creator = new JwtTokenProvider(SECRET_KEY, 30);
        JwtTokenProvider parser = new JwtTokenProvider(
                "Pjdt3NQ5vLBG1s2PKv9nx2xGk36h5mU7fVPT0n7rQZzwgt9M",
                30
        );

        String token = creator.create(15L);

        assertThatThrownBy(() -> parser.parse(token))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.AUTHENTICATION_TOKEN_INVALID);
    }

    @Test
    void parseThrowWhenTokenIsExpired() {
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(SECRET_KEY, 30);
        Date now = new Date();
        Date issuedAt = new Date(now.getTime() - 60_000L);
        Date expiredAt = new Date(now.getTime() - 1_000L);
        String expiredToken = Jwts.builder()
                .subject("21")
                .issuedAt(issuedAt)
                .expiration(expiredAt)
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                .compact();

        assertThatThrownBy(() -> jwtTokenProvider.parse(expiredToken))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.AUTHENTICATION_TOKEN_INVALID);
    }

    @Test
    void parseThrowWhenSubjectIsNotNumber() {
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(SECRET_KEY, 30);
        Date now = new Date();
        Date expiration = new Date(now.getTime() + 60_000L);
        String invalidSubjectToken = Jwts.builder()
                .subject("member-1")
                .issuedAt(now)
                .expiration(expiration)
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                .compact();

        assertThatThrownBy(() -> jwtTokenProvider.parse(invalidSubjectToken))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.AUTHENTICATION_TOKEN_INVALID);
    }
}
