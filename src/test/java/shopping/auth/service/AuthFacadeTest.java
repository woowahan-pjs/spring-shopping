package shopping.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import shopping.auth.domain.DuplicateEmailException;
import shopping.auth.domain.InvalidUserException;
import shopping.auth.domain.Role;
import shopping.auth.dto.LoginRequest;
import shopping.auth.dto.LoginResponse;
import shopping.auth.dto.RegisterRequest;
import shopping.auth.dto.RegisterResponse;
import shopping.auth.dto.UserResponse;
import shopping.infra.security.JwtTokenProvider;
import shopping.infra.security.UserPrincipal;

@ExtendWith(MockitoExtension.class)
class AuthFacadeTest {

    @InjectMocks private AuthFacade authFacade;

    @Mock private UserService userService;

    @Mock private JwtTokenProvider jwtTokenProvider;

    @Nested
    @DisplayName("회원 가입을 할 때,")
    class register {

        @Test
        @DisplayName("이메일이 중복일 때, 예외가 발생합니다.")
        void duplicate() {
            // given
            final RegisterRequest request =
                    new RegisterRequest("test@test@.com", "12345678", Role.ADMIN);

            given(userService.register(request)).willThrow(new DuplicateEmailException());

            // when & then
            assertThatThrownBy(() -> authFacade.register(request))
                    .isInstanceOf(DuplicateEmailException.class)
                    .hasMessage("이미 등록된 이메일입니다.");
        }

        @Test
        @DisplayName("성공했다면, accessToken을 반환합니다.")
        void success() {
            // given
            final String expectedAccessToken = "ACCESS-TOKEN";

            final RegisterRequest request =
                    new RegisterRequest("test@test@.com", "12345678", Role.ADMIN);

            given(userService.register(request)).willReturn(703L);
            given(jwtTokenProvider.generateToken(any(UserPrincipal.class)))
                    .willReturn(expectedAccessToken);

            // when
            final RegisterResponse result = authFacade.register(request);

            // then
            assertThat(result.accessToken()).isEqualTo(expectedAccessToken);
        }
    }

    @Nested
    @DisplayName("로그인을 할 때,")
    class login {

        @Test
        @DisplayName("존재하지 않는 회원이거나, 비밀번호가 동일하지 않은 경우 예외가 발생합니다.")
        void invalidUser() {
            // given
            final LoginRequest request = new LoginRequest("test@test.com", "12345678");

            given(userService.getUser(request.email(), request.password()))
                    .willThrow(new InvalidUserException());

            // when & then
            assertThatThrownBy(() -> authFacade.login(request))
                    .isInstanceOf(InvalidUserException.class)
                    .hasMessage("이메일 또는 비밀번호를 확인해주세요.");
        }

        @Test
        @DisplayName("성공적으로 로그인을하여 accessToken을 반환합니다.")
        void success() {
            // given
            final String email = "test@test.com";
            final LoginRequest request = new LoginRequest(email, "12345678");

            final UserResponse userResponse = new UserResponse(1L, email, Role.CUSTOMER);

            final String expectedToken = "ACCESS-TOKEN";

            given(userService.getUser(request.email(), request.password()))
                    .willReturn(userResponse);
            given(jwtTokenProvider.generateToken(any(UserPrincipal.class)))
                    .willReturn(expectedToken);

            // when
            final LoginResponse response = authFacade.login(request);

            // then
            assertThat(response.accessToken()).isEqualTo(expectedToken);
        }
    }
}
