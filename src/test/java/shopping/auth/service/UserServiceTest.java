package shopping.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import shopping.auth.domain.DuplicateEmailException;
import shopping.auth.domain.Role;
import shopping.auth.domain.User;
import shopping.auth.domain.UserFixture;
import shopping.auth.dto.RegisterRequest;
import shopping.auth.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks private UserService userService;

    @Mock private UserRepository userRepository;

    @Mock private PasswordEncoder passwordEncoder;

    @Nested
    @DisplayName("회원 가입을 할 때,")
    class register {

        @Test
        @DisplayName("이미 가입된 이메일의 경우, 예외가 발생합니다.")
        void duplicate() {
            // given
            final String email = "test@test.com";
            final String password = "12345678";
            final Role role = Role.CUSTOMER;

            final RegisterRequest request = new RegisterRequest(email, password, role);

            given(userRepository.existsByEmail(email)).willReturn(true);

            // when & then
            assertThatThrownBy(() -> userService.register(request))
                    .isInstanceOf(DuplicateEmailException.class)
                    .hasMessage("이미 등록된 이메일입니다.");
        }

        @Test
        @DisplayName("성공적으로 회원 가입을 합니다.")
        void success() {
            // given
            final String email = "test@test.com";
            final String password = "12345678";
            final Role role = Role.CUSTOMER;

            final RegisterRequest request = new RegisterRequest(email, password, role);

            final Long expectedId = 703L;
            final User expectedUser = UserFixture.fixture(expectedId, email, password, role);

            given(userRepository.existsByEmail(email)).willReturn(false);
            given(passwordEncoder.encode(password)).willReturn("ENCODE-PASSWORD");
            given(userRepository.save(any(User.class))).willReturn(expectedUser);

            // when
            final Long userId = userService.register(request);

            // then
            assertThat(userId).isEqualTo(expectedId);
        }
    }
}
