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
import shopping.auth.domain.Role;
import shopping.auth.dto.RegisterRequest;
import shopping.auth.dto.RegisterResponse;
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
}
