package shopping.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shopping.auth.domain.RefreshToken;
import shopping.auth.domain.RefreshTokenRepository;
import shopping.common.ApiException;
import shopping.common.ErrorCode;

@ExtendWith(MockitoExtension.class)
class RefreshTokenManagerTest {
    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Captor
    private ArgumentCaptor<RefreshToken> refreshTokenCaptor;

    private RefreshTokenManager refreshTokenManager;

    @BeforeEach
    void setUp() {
        refreshTokenManager = new RefreshTokenManager(refreshTokenRepository, 7L);
    }

    @Test
    @DisplayName("refresh token을 발급하면 원문과 해시를 분리해서 저장한다")
    void issueSaveHashedRefreshToken() {
        // given
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        String rawRefreshToken = refreshTokenManager.issue(1L);

        // then
        verify(refreshTokenRepository).save(refreshTokenCaptor.capture());
        RefreshToken savedToken = refreshTokenCaptor.getValue();
        assertThat(rawRefreshToken).isNotBlank();
        assertThat(savedToken.getMemberId()).isEqualTo(1L);
        assertThat(savedToken.getTokenHash()).isNotBlank();
        assertThat(savedToken.getTokenHash()).isNotEqualTo(rawRefreshToken);
        assertThat(savedToken.getExpiresAt()).isAfter(LocalDateTime.now().plusDays(6));
    }

    @Test
    @DisplayName("유효한 refresh token이면 저장된 토큰 엔티티를 돌려준다")
    void getValidTokenReturnSavedToken() {
        // given
        RefreshToken savedToken = RefreshToken.issue(1L, "hashed-refresh-token", LocalDateTime.now().plusDays(1));
        when(refreshTokenRepository.findByTokenHash(anyString())).thenReturn(Optional.of(savedToken));

        // when
        RefreshToken refreshToken = refreshTokenManager.getValidToken("raw-refresh-token");

        // then
        assertThat(refreshToken).isEqualTo(savedToken);
    }

    @Test
    @DisplayName("refresh token이 없으면 인증을 거부한다")
    void getValidTokenRejectNullToken() {
        // given

        // when
        // then
        assertThatThrownBy(() -> refreshTokenManager.getValidToken(null))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.REFRESH_TOKEN_REQUIRED);
    }

    @Test
    @DisplayName("만료한 refresh token은 삭제하고 인증을 거부한다")
    void getValidTokenRejectExpiredToken() {
        // given
        RefreshToken expiredToken = RefreshToken.issue(1L, "hashed-refresh-token", LocalDateTime.now().minusSeconds(1));
        when(refreshTokenRepository.findByTokenHash(anyString())).thenReturn(Optional.of(expiredToken));

        // when
        // then
        assertThatThrownBy(() -> refreshTokenManager.getValidToken("raw-refresh-token"))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.REFRESH_TOKEN_INVALID);

        verify(refreshTokenRepository).delete(expiredToken);
    }

    @Test
    @DisplayName("refresh token을 회전하면 토큰 해시와 만료 시간이 바뀐다")
    void rotateUpdateTokenHashAndExpiresAt() {
        // given
        RefreshToken refreshToken = RefreshToken.issue(1L, "old-hash", LocalDateTime.now().plusDays(1));
        LocalDateTime previousExpiresAt = refreshToken.getExpiresAt();

        // when
        String rotatedRefreshToken = refreshTokenManager.rotate(refreshToken);

        // then
        assertThat(rotatedRefreshToken).isNotBlank();
        assertThat(refreshToken.getTokenHash()).isNotEqualTo("old-hash");
        assertThat(refreshToken.getExpiresAt()).isAfterOrEqualTo(previousExpiresAt.truncatedTo(ChronoUnit.SECONDS));
    }
}
