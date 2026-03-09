package shopping.auth.adapter.out;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shopping.auth.domain.JwtSubject;
import shopping.common.ApiException;
import shopping.common.ErrorCode;

@DisplayName("[인증] JWT 토큰 제공자 단위 테스트")
class JwtTokenProviderTest {
    private static final String SECRET_KEY = "sJ55n8qtAW1mWL7v0ltWb6N7zseMqnPw5Y6x8tl1b2kvVDRj";

    @Test
    @DisplayName("토큰을 만들고 다시 파싱하면 같은 회원 id가 나온다")
    void createAndParseReturnSameMemberId() {
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(SECRET_KEY, 30);

        String token = jwtTokenProvider.create(99L);
        JwtSubject subject = jwtTokenProvider.parse(token);

        assertThat(subject.memberId()).isEqualTo(99L);
    }

    @Test
    @DisplayName("토큰이 변조되면 파싱하지 못한다")
    void parseThrowWhenTokenIsTampered() {
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(SECRET_KEY, 30);

        String token = jwtTokenProvider.create(1L) + "tampered";

        assertThatThrownBy(() -> jwtTokenProvider.parse(token))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.AUTHENTICATION_TOKEN_INVALID);
    }

    @Test
    @DisplayName("다른 시크릿으로 서명한 토큰은 파싱하지 못한다")
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
    @DisplayName("만료된 토큰은 파싱하지 못한다")
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
    @DisplayName("subject가 숫자가 아니면 파싱하지 못한다")
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
