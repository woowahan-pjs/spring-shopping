package shopping.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shopping.auth.domain.RefreshToken;
import shopping.auth.domain.JwtSubject;
import shopping.auth.adapter.out.JwtTokenProvider;
import shopping.common.ApiException;
import shopping.common.ErrorCode;
import shopping.member.domain.Member;
import shopping.member.domain.MemberFixture;
import shopping.member.domain.MemberRepository;
import shopping.member.domain.MemberRole;
import shopping.member.domain.MemberStatus;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private RefreshTokenManager refreshTokenManager;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(jwtTokenProvider, memberRepository, refreshTokenManager);
    }

    @Test
    @DisplayName("토큰을 발급하면 access token과 refresh token을 함께 만든다")
    void issueTokensCreateAccessAndRefreshTokens() {
        // given
        when(jwtTokenProvider.create(10L)).thenReturn("access-token");
        when(refreshTokenManager.issue(10L)).thenReturn("refresh-token");

        // when
        AuthTokens tokens = authService.issueTokens(10L);

        // then
        assertThat(tokens.accessToken()).isEqualTo("access-token");
        assertThat(tokens.refreshToken()).isEqualTo("refresh-token");
        verify(jwtTokenProvider).create(10L);
        verify(refreshTokenManager).issue(10L);
    }

    @Test
    @DisplayName("refresh token을 사용하면 access token을 다시 발급하고 refresh token을 회전한다")
    void refreshRotateTokenWhenRefreshTokenIsValid() {
        // given
        RefreshToken refreshToken = refreshToken(7L, LocalDateTime.now().plusDays(1));
        Member member = member(7L, MemberStatus.ACTIVE);
        when(refreshTokenManager.getValidToken("valid-refresh-token")).thenReturn(refreshToken);
        when(memberRepository.findById(7L)).thenReturn(Optional.of(member));
        when(jwtTokenProvider.create(7L)).thenReturn("rotated-access-token");
        when(refreshTokenManager.rotate(refreshToken)).thenReturn("rotated-refresh-token");

        // when
        AuthTokens tokens = authService.refresh("valid-refresh-token");

        // then
        assertThat(tokens.accessToken()).isEqualTo("rotated-access-token");
        assertThat(tokens.refreshToken()).isEqualTo("rotated-refresh-token");
    }

    @Test
    @DisplayName("refresh 대상 회원이 없으면 인증을 거부한다")
    void refreshThrowWhenMemberDoesNotExist() {
        // given
        RefreshToken refreshToken = refreshToken(7L, LocalDateTime.now().plusDays(1));
        when(refreshTokenManager.getValidToken("valid-refresh-token")).thenReturn(refreshToken);
        when(memberRepository.findById(7L)).thenReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> authService.refresh("valid-refresh-token"))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.REFRESH_TOKEN_INVALID);
    }

    @Test
    @DisplayName("refresh 대상 회원이 비활성이면 인증을 거부한다")
    void refreshThrowWhenMemberIsInactive() {
        // given
        RefreshToken refreshToken = refreshToken(7L, LocalDateTime.now().plusDays(1));
        Member member = member(null, MemberStatus.INACTIVE);
        when(refreshTokenManager.getValidToken("valid-refresh-token")).thenReturn(refreshToken);
        when(memberRepository.findById(7L)).thenReturn(Optional.of(member));

        // when
        // then
        assertThatThrownBy(() -> authService.refresh("valid-refresh-token"))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.REFRESH_TOKEN_INVALID);
    }

    @Test
    @DisplayName("Authorization 헤더가 유효하면 회원 id를 돌려준다")
    void authenticateReturnMemberIdWhenAuthorizationHeaderIsValid() {
        // given
        when(jwtTokenProvider.parse("valid-token")).thenReturn(new JwtSubject(7L));

        // when
        Long memberId = authService.authenticate("Bearer valid-token");

        // then
        assertThat(memberId).isEqualTo(7L);
        verify(jwtTokenProvider).parse("valid-token");
    }

    @Test
    @DisplayName("Authorization 헤더 공백은 제거하고 파싱한다")
    void authenticateTrimTokenBeforeParse() {
        // given
        when(jwtTokenProvider.parse("trimmed-token")).thenReturn(new JwtSubject(3L));

        // when
        Long memberId = authService.authenticate("Bearer   trimmed-token  ");

        // then
        assertThat(memberId).isEqualTo(3L);
        verify(jwtTokenProvider).parse("trimmed-token");
    }

    @Test
    @DisplayName("Authorization 헤더가 없으면 인증을 거부한다")
    void authenticateThrowWhenAuthorizationHeaderIsNull() {
        // given

        // when
        // then
        assertThatThrownBy(() -> authService.authenticate(null))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.AUTHORIZATION_HEADER_REQUIRED);
    }

    @Test
    @DisplayName("Authorization 헤더가 비어 있으면 인증을 거부한다")
    void authenticateThrowWhenAuthorizationHeaderIsBlank() {
        // given

        // when
        // then
        assertThatThrownBy(() -> authService.authenticate("   "))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.AUTHORIZATION_HEADER_REQUIRED);
    }

    @Test
    @DisplayName("Authorization 헤더가 Bearer 형식이 아니면 인증을 거부한다")
    void authenticateThrowWhenAuthorizationHeaderIsNotBearerFormat() {
        // given

        // when
        // then
        assertThatThrownBy(() -> authService.authenticate("Token abc"))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.AUTHORIZATION_HEADER_INVALID);
    }

    private RefreshToken refreshToken(Long memberId, LocalDateTime expiresAt) {
        return RefreshToken.issue(memberId, "hashed-refresh-token", expiresAt);
    }

    private Member member(Long memberId, MemberStatus status) {
        return MemberFixture.member(memberId, "user@example.com", "password", status, MemberRole.USER);
    }
}
