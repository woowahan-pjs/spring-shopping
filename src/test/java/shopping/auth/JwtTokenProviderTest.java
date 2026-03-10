package shopping.auth;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class JwtTokenProviderTest {

    @Autowired
    private TokenProvider tokenProvider;

    @Test
    @DisplayName("memberId로 토큰을 생성할 수 있다")
    void test01() {
        // given
        Long memberId = 1L;

        // when
        String token = tokenProvider.createToken(memberId);

        // then
        assertThat(token).isNotBlank();
    }

    @Test
    @DisplayName("생성된 토큰에서 memberId를 추출할 수 있다")
    void test02() {
        // given
        Long memberId = 42L;
        String token = tokenProvider.createToken(memberId);

        // when
        Long extracted = tokenProvider.getMemberId(token);

        // then
        assertThat(extracted).isEqualTo(memberId);
    }

    @Test
    @DisplayName("유효하지 않은 토큰으로 memberId를 추출하면 예외가 발생한다")
    void test03() {
        // given
        String invalidToken = "invalid.token.value";

        // when & then
        assertThatThrownBy(() -> tokenProvider.getMemberId(invalidToken))
            .isInstanceOf(JwtException.class);
    }

    @Test
    @DisplayName("만료된 토큰으로 memberId를 추출하면 예외가 발생한다")
    void test04() {
        // arrange
        JwtProperties expiredProperties = new JwtProperties(
            "spring-shopping-jwt-secret-key-must-be-long-enough",
            -1L
        );
        JwtTokenProvider expiredTokenProvider = new JwtTokenProvider(expiredProperties);
        String expiredToken = expiredTokenProvider.createToken(1L);

        // act & assert
        assertThatThrownBy(() -> expiredTokenProvider.getMemberId(expiredToken))
            .isInstanceOf(JwtException.class);
    }
}
