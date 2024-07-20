package shopping.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import shopping.member.application.MemberNotFoundException;
import shopping.member.application.MemberService;
import shopping.member.application.TestMemberRepository;
import shopping.member.domain.MemberCreate;
import shopping.token.application.TokenService;
import shopping.token.domain.Token;

class AuthenticationServiceTest {
    private static final String JWT_SECRET = "dGVzdA==dGVzdA==dGVzdA==dGVzdA==dGVzdA==dGVzdA==dGVzdA==dGVzdA==dGVzdA==dGVzdA==dGVzdA==dGVzdA==dGVzdA==";

    private AuthenticationService authenticationService;
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        memberService = new MemberService(new TestMemberRepository(), passwordEncoder);

        authenticationService = new AuthenticationService(
                memberService,
                passwordEncoder,
                new TokenService(JWT_SECRET, 60)
        );
    }

    @DisplayName("가입한 회원의 이메일과 비밀번호가 일치하면 Token을 발행한다")
    @Test
    void test() {
        // given
        String email = "email@email.com";
        String password = "password";
        memberService.register(new MemberCreate(email, password));

        // when
        Token token = authenticationService.authenticate(new LoginRequest(email, password));

        // then
        assertThat(token).isNotNull();
        assertThat(token.getValue()).isNotBlank();
    }

    @DisplayName("가입한 회원의 이메일과 비밀번호가 일치하지 않으면 예외를 발생시킨다")
    @Test
    void test2() {
        // given
        String email = "email@email.com";
        memberService.register(new MemberCreate(email, "password"));

        // when
        // then
        String invalidPassword = "invalid";
        assertThatThrownBy(() -> authenticationService.authenticate(new LoginRequest(email, invalidPassword)))
                .isInstanceOf(NotMatchedPasswordException.class);
    }

    @DisplayName("해당하는 email로 가입한 회원이 없으면 예외를 발생시킨다.")
    @Test
    void test3() {
        // given
        String email = "email@email.com";
        memberService.register(new MemberCreate(email, "password"));

        // when
        // then
        String invalidEmail = "invalid@invalid.com";
        assertThatThrownBy(() -> authenticationService.authenticate(new LoginRequest(invalidEmail, "password")))
                .isInstanceOf(MemberNotFoundException.class);
    }
}