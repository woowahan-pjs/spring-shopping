package shopping.application.member;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import shopping.domain.member.Member;
import shopping.domain.repository.MemberRepository;
import shopping.dto.SignUpRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
}