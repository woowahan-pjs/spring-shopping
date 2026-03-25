package shopping.member.adapter.in.api;

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
import org.springframework.mock.web.MockHttpServletResponse;
import shopping.auth.adapter.in.api.TokenResponse;
import shopping.auth.adapter.in.web.RefreshTokenCookieManager;
import shopping.auth.service.AuthTokens;
import shopping.member.service.MemberService;

@ExtendWith(MockitoExtension.class)
@DisplayName("[회원] 회원 컨트롤러 단위 테스트")
class MemberControllerTest {
    @Mock
    private MemberService memberService;

    @Mock
    private RefreshTokenCookieManager refreshTokenCookieManager;

    @Test
    @DisplayName("회원 가입은 access token과 refresh cookie를 내려준다")
    void registerReturnCreatedTokenResponse() {
        // given
        MemberController controller = new MemberController(memberService, refreshTokenCookieManager);
        RegisterRequest request = new RegisterRequest("user@example.com", "password123");
        MockHttpServletResponse response = new MockHttpServletResponse();
        when(memberService.register(request)).thenReturn(new AuthTokens("access-token", "refresh-token"));

        // when
        ResponseEntity<TokenResponse> result = controller.register(request, response);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isEqualTo(TokenResponse.from(new AuthTokens("access-token", "refresh-token")));
        verify(refreshTokenCookieManager).write(response, "refresh-token");
    }

    @Test
    @DisplayName("로그인은 access token과 refresh cookie를 내려준다")
    void loginReturnOkTokenResponse() {
        // given
        MemberController controller = new MemberController(memberService, refreshTokenCookieManager);
        LoginRequest request = new LoginRequest("user@example.com", "password123");
        MockHttpServletResponse response = new MockHttpServletResponse();
        when(memberService.login(request)).thenReturn(new AuthTokens("access-token", "refresh-token"));

        // when
        ResponseEntity<TokenResponse> result = controller.login(request, response);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(TokenResponse.from(new AuthTokens("access-token", "refresh-token")));
        verify(refreshTokenCookieManager).write(response, "refresh-token");
    }
}
