package shopping.member;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RegisterMemberServiceTest {

    @Autowired
    private RegisterMember registerMember;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void 회원을_등록한다() {
        String token = registerMember.execute("register@test.com", "password123");

        assertNotNull(token);
        assertTrue(memberRepository.existsByEmail("register@test.com"));
    }

    @Test
    void 등록한_회원이_로그인할_수_있다() {
        registerMember.execute("login-check@test.com", "password123");

        Member saved = memberRepository.findByEmail("login-check@test.com").orElseThrow();
        saved.login("password123", passwordEncoder);
    }

    @Test
    void 이미_존재하는_이메일이면_예외가_발생한다() {
        registerMember.execute("duplicate@test.com", "password123");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> registerMember.execute("duplicate@test.com", "password456"));

        assertEquals("이미 존재하는 이메일입니다.", exception.getMessage());
    }

    @Test
    void 비밀번호가_8자_미만이면_예외가_발생한다() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> registerMember.execute("short-pw@test.com", "short"));

        assertEquals("비밀번호는 8자 이상이어야 합니다.", exception.getMessage());
    }
}
