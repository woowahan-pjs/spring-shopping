package shopping.application.member;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import shopping.domain.member.Member;
import shopping.domain.member.exception.LoginFailedException;
import shopping.domain.repository.MemberRepository;
import shopping.dto.LoginRequest;
import shopping.dto.LoginResponse;
import shopping.dto.SignUpRequest;
import shopping.infrastructure.auth.JwtTokenProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class MemberServiceTest {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("회원 가입에 성공하고, 비밀번호는 암호화되어 저장되어야 한다.")
    void signUp_success_and_password_encrypted() {
        String rawPassword = "password123!";
        SignUpRequest request = new SignUpRequest("dev@example.com", rawPassword);

        // when
        Long savedId = memberService.signUp(request);

        // then
        Member foundMember = memberRepository.findById(savedId)
                .orElseThrow(() -> new AssertionError("회원이 정상적으로 저장되지 않았습니다."));

        assertThat(foundMember.getEmail()).isEqualTo("dev@example.com");
        assertThat(foundMember.getPassword()).isNotEqualTo(rawPassword);
        assertThat(passwordEncoder.matches(rawPassword, foundMember.getPassword())).isTrue();
    }

    @Test
    @DisplayName("이메일과 비밀번호로 맞으면 JWT 토큰이 발급되어야 한다.")
    void login_success() {
        String email = "login@example.com";
        String password = "password123!";
        memberService.signUp(new SignUpRequest(email, password));

        LoginResponse response = memberService.login(new LoginRequest(email, password));

        // then
        assertThat(response.accessToken()).isNotBlank();

        // 토큰이 유효한지 provider를 통해 한 번 더 검증 (선택)
        assertThat(jwtTokenProvider.validateToken(response.accessToken())).isTrue();
        assertThat(jwtTokenProvider.getPayload(response.accessToken())).isEqualTo(email);
    }

    @Test
    @DisplayName("비밀번호가 틀리면 로그인에 실패하고 예외가 발생해야 한다.")
    void login_fail_with_wrong_password() {
        // given
        String email = "wrong_pw@example.com";
        memberService.signUp(new SignUpRequest(email, "correct_password"));

        // when & then
        assertThatThrownBy(() -> memberService.login(new LoginRequest(email, "wrong_password")))
                .isInstanceOf(LoginFailedException.class);
    }
}