package shopping.token.application;

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
import shopping.token.domain.Token;
import shopping.token.domain.TokenProvider;

class TokenServiceTest {
    private static final String JWT_SECRET = "dGVzdA==dGVzdA==dGVzdA==dGVzdA==dGVzdA==dGVzdA==dGVzdA==dGVzdA==dGVzdA==dGVzdA==dGVzdA==dGVzdA==dGVzdA==";

    private TokenService tokenService;
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        memberService = new MemberService(new TestMemberRepository(), passwordEncoder);

        tokenService = new TokenService(
                memberService,
                passwordEncoder,
                new TokenProvider(JWT_SECRET, 60)
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
        Token token = tokenService.generate(new LoginRequest(email, password));

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
        assertThatThrownBy(() -> tokenService.generate(new LoginRequest(email, invalidPassword)))
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
        assertThatThrownBy(() -> tokenService.generate(new LoginRequest(invalidEmail, "password")))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @DisplayName("authorizationHeader 값으로 부터 email을 추출할 수 있다.")
    @Test
    void test4() {
        // given
        String email = "email@email.com";
        String password = "password";
        memberService.register(new MemberCreate(email, password));
        Token token = tokenService.generate(new LoginRequest(email, password));

        // when
        String extractedEmail = tokenService.extractEmail("Bearer " + token.getValue());

        // then
        assertThat(extractedEmail).isEqualTo(email);
    }

}