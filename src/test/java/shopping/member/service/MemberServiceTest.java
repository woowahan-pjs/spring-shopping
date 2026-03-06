package shopping.member.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shopping.member.repository.MemberRepository;
import shopping.member.service.dto.MemberLoginInput;
import shopping.member.service.dto.MemberRegisterInput;
import shopping.member.service.dto.TokenOutput;

import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class MemberServiceTest {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("이메일과 비밀번호로 회원가입 성공")
    void test01() {
        // given
        MemberRegisterInput request = new MemberRegisterInput("user@example.com", "password123");

        // when
        TokenOutput sut = memberService.register(request);

        // then
        assertThat(sut.token()).isNotNull();
    }

    @Test
    @DisplayName("중복 이메일로 회원가입 시 예외 발생")
    void test02() {
        // given
        MemberRegisterInput request = new MemberRegisterInput("user@example.com", "password123");
        memberService.register(request);

        // when & then
        assertThatThrownBy(() -> memberService.register(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("이미 사용 중인 이메일입니다.");
    }

    @Test
    @DisplayName("이메일과 비밀번호로 로그인 성공")
    void test03() {
        // given
        memberService.register(new MemberRegisterInput("user@example.com", "password123"));
        MemberLoginInput request = new MemberLoginInput("user@example.com", "password123");

        // when
        TokenOutput sut = memberService.login(request);

        // then
        assertThat(sut.token()).isNotNull();
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 로그인 시 예외 발생")
    void test04() {
        // given
        MemberLoginInput request = new MemberLoginInput("notfound@example.com", "password123");

        // when & then
        assertThatThrownBy(() -> memberService.login(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("존재하지 않는 이메일입니다.");
    }

    @Test
    @DisplayName("회원가입 시 비밀번호는 암호화되어 저장된다")
    void test06() {
        // given
        String rawPassword = "password123";
        MemberRegisterInput request = new MemberRegisterInput("user@example.com", rawPassword);

        // when
        memberService.register(request);

        // then
        String storedPassword = memberRepository.findByEmail("user@example.com")
            .get().getPassword();
        assertThat(storedPassword).isNotEqualTo(rawPassword);
    }

    @Test
    @DisplayName("비밀번호 불일치로 로그인 시 예외 발생")
    void test05() {
        // given
        memberService.register(new MemberRegisterInput("user@example.com", "password123"));
        MemberLoginInput request = new MemberLoginInput("user@example.com", "wrongpassword");

        // when & then
        assertThatThrownBy(() -> memberService.login(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("비밀번호가 일치하지 않습니다.");
    }
}
