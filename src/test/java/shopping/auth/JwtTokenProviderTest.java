package shopping.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static shopping.member.domain.MemberFixture.*;

@DisplayName("JWT 토큰 프로바이더")
class JwtTokenProviderTest {
    private final String accessSecret = "dGVzdC1zZWNyZXQta2V5LWZvci10ZXN0aW5nLW9ubHk=";
    private final String refreshSecret = "dGVzdC1yZWZyZXNoLXNlY3JldC1rZXktZm9yLWp3dC1obWFjLXNoYTI1Ng==";
    private JwtTokenProvider provider;

    @BeforeEach
    void setUp() {
        provider = new JwtTokenProvider(accessSecret, refreshSecret, 3600000L, 604800000L);
    }

    @Test
    @DisplayName("토큰 발급")
    void generatedToken() {
        String token = provider.generateAccessToken(createWithId(1L));

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
        JwtTokenProvider provider1 = new JwtTokenProvider(accessSecret, refreshSecret, 1L, 1L);
        String token = provider1.generateAccessToken(createWithId(1L));
        Thread.sleep(10);

        assertThatThrownBy(() -> provider1.extract(token))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("리프래쉬 토큰으로 멤버id 복원")
    void refreshTokenToMemberId() {
        String refreshToken = provider.generateRefreshToken(createWithId(1L));

        assertThat(provider.extractMemberIdFromRefreshToken(refreshToken)).isEqualTo(1L);
    }

    @Test
    @DisplayName("리프레쉬 토큰 잘못 인증")
    void invalidRefreshToken() {
        assertThatThrownBy(() -> provider.extractMemberIdFromRefreshToken("invalid.token"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("만료된 리프레쉬 토큰 인증 실패")
    void expiredRefreshToken() throws InterruptedException {
        JwtTokenProvider provider1 = new JwtTokenProvider(accessSecret, refreshSecret, 1L, 1L);
        String token = provider1.generateRefreshToken(createWithId(1L));
        Thread.sleep(10);

        assertThatThrownBy(() -> provider1.extractMemberIdFromRefreshToken(token))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("access 토큰을 refresh 검증에 넣으면 실패")
    void accessTokenCannotBeUsedAsRefresh() {
        String accessToken = provider.generateAccessToken(createWithId(1L));

        assertThatThrownBy(() -> provider.extractMemberIdFromRefreshToken(accessToken))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("refresh 토큰을 access 검증에 넣으면 실패")
    void refreshTokenCannotBeUsedAsAccess() {
        String refreshToken = provider.generateRefreshToken(createWithId(1L));

        assertThatThrownBy(() -> provider.extract(refreshToken))
                .isInstanceOf(IllegalArgumentException.class);
    }
}