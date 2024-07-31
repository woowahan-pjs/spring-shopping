package shopping.auth.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import shopping.auth.application.dto.TokenInfo;
import shopping.auth.exception.AuthenticationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtTokenProviderTest {
    private static final long TOKEN_ID = 1L;
    private static final String TOKEN_EMAIL = "test@test.com";
    private static final String EXPIRED_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiZW1haWwiOiJ0ZXN0QHRlc3QuY29tIiwiaWF0IjoxNzIxNDY2NDc5LCJleHAiOjE3MjE0NjY0ODl9.iFLFthZgfX-Z6fnupTTv8Rvk7f39lxsek9_mUjfoEfM";
    private JwtTokenProvider jwtTokenProvider;
    private String token;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider("testSecretKeytestSecretKeytestSecretKeytestSecretKey", 1000 * 10);
        token = jwtTokenProvider.createToken(TOKEN_ID, TOKEN_EMAIL);
    }

    @Test
    @DisplayName("토큰을 생성할 수 있다")
    void createTokenTest() {
        assertThat(token).isNotBlank();
    }

    @Test
    @DisplayName("토큰정보를 토대로 TokenInfo 를 추출할 수 있다")
    void getTokenInfoTest() {
        final TokenInfo actual = jwtTokenProvider.getTokenInfo(token);
        final TokenInfo expected = new TokenInfo(TOKEN_ID, TOKEN_EMAIL);

        System.out.println("token = " + token);

        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
            EXPIRED_TOKEN,
            "invalidToken"
    })
    @DisplayName("유효하지 않은 토큰은 예외를 던진다")
    void invalidTokenTest(final String token) {
        assertThatThrownBy(() -> jwtTokenProvider.getTokenInfo(token))
                .isInstanceOf(AuthenticationException.class);
    }
}
