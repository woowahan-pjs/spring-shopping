package shopping.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static shopping.member.domain.MemberFixture.*;

class JwtTokenProviderTest {
    private final String secret = "dGVzdC1zZWNyZXQta2V5LWZvci10ZXN0aW5nLW9ubHk=";
    private JwtTokenProvider provider;

    @BeforeEach
    void setUp() {
        provider = new JwtTokenProvider(secret, 3600000L);
    }

    @Test
    @DisplayName("토큰 발급")
    void generatedToken() {
        String token = provider.generate(createWithId(1L));

        assertThat(provider.extract(token)).isEqualTo(1L);
    }

    @Test
    @DisplayName("변조된 토큰 인증 실패")
    void invalidToken() {
        assertThatThrownBy(() -> provider.extract("invalid.token"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("만료된 토큰 인증 실패")
    void expiredToken() throws InterruptedException {
        JwtTokenProvider provider1 = new JwtTokenProvider(secret, 1L);
        String token = provider1.generate(createWithId(1L));
        Thread.sleep(10);

        assertThatThrownBy(() -> provider1.extract(token))
                .isInstanceOf(IllegalArgumentException.class);
    }
}