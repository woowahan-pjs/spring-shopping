package shopping.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shopping.auth.token.JwtSubject;
import shopping.auth.token.JwtTokenProvider;
import shopping.common.ApiException;
import shopping.common.ErrorCode;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(jwtTokenProvider);
    }

    @Test
    void issueTokenCreateJwtToken() {
        when(jwtTokenProvider.create(10L)).thenReturn("token-value");

        String token = authService.issueToken(10L);

        assertThat(token).isEqualTo("token-value");
        verify(jwtTokenProvider).create(10L);
    }

    @Test
    void authenticateReturnMemberIdWhenAuthorizationHeaderIsValid() {
        when(jwtTokenProvider.parse("valid-token")).thenReturn(new JwtSubject(7L));

        Long memberId = authService.authenticate("Bearer valid-token");

        assertThat(memberId).isEqualTo(7L);
        verify(jwtTokenProvider).parse("valid-token");
    }

    @Test
    void authenticateTrimTokenBeforeParse() {
        when(jwtTokenProvider.parse("trimmed-token")).thenReturn(new JwtSubject(3L));

        Long memberId = authService.authenticate("Bearer   trimmed-token  ");

        assertThat(memberId).isEqualTo(3L);
        verify(jwtTokenProvider).parse("trimmed-token");
    }

    @Test
    void authenticateThrowWhenAuthorizationHeaderIsNull() {
        assertThatThrownBy(() -> authService.authenticate(null))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.AUTHORIZATION_HEADER_REQUIRED);
    }

    @Test
    void authenticateThrowWhenAuthorizationHeaderIsBlank() {
        assertThatThrownBy(() -> authService.authenticate("   "))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.AUTHORIZATION_HEADER_REQUIRED);
    }

    @Test
    void authenticateThrowWhenAuthorizationHeaderIsNotBearerFormat() {
        assertThatThrownBy(() -> authService.authenticate("Token abc"))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.AUTHORIZATION_HEADER_INVALID);
    }
}
