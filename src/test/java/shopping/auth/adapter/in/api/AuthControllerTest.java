package shopping.auth.adapter.in.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import shopping.auth.adapter.in.web.RefreshTokenCookieManager;
import shopping.auth.service.AuthService;
import shopping.auth.service.AuthTokens;

@ExtendWith(MockitoExtension.class)
@DisplayName("[인증] 인증 컨트롤러 단위 테스트")
class AuthControllerTest {
    @Mock
    private AuthService authService;

    @Mock
    private RefreshTokenCookieManager refreshTokenCookieManager;

    @Test
    @DisplayName("refresh 요청이 들어오면 새 access token과 refresh cookie를 내려준다")
    void refreshIssueNewTokens() {
        // given
        AuthController authController = new AuthController(authService, refreshTokenCookieManager);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        when(refreshTokenCookieManager.resolve(request)).thenReturn("refresh-token");
        when(authService.refresh("refresh-token")).thenReturn(new AuthTokens("new-access-token", "new-refresh-token"));

        // when
        ResponseEntity<TokenResponse> result = authController.refresh(request, response);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(TokenResponse.from(new AuthTokens("new-access-token", "new-refresh-token")));
        verify(refreshTokenCookieManager).resolve(request);
        verify(refreshTokenCookieManager).write(response, "new-refresh-token");
    }
}
