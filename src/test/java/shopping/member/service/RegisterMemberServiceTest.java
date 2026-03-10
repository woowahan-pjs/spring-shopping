package shopping.member.service;

import shopping.member.domain.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegisterMemberServiceTest {

    private InMemoryMemberRepository memberRepository;
    private RegisterMemberService service;

    @BeforeEach
    void setUp() {
        memberRepository = new InMemoryMemberRepository();
        service = new RegisterMemberService(memberRepository, new FakePasswordEncoder());
    }

    @Test
    void 회원을_등록한다() {
        String token = service.execute("test@test.com", "password123");

        assertNotNull(token);
        assertTrue(memberRepository.existsByEmail("test@test.com"));
    }

    @Test
    void 비밀번호가_인코딩되어_저장된다() {
        service.execute("test@test.com", "password123");

        Member saved = memberRepository.findByEmail("test@test.com").orElseThrow();
        FakePasswordEncoder encoder = new FakePasswordEncoder();
        assertTrue(encoder.matches("password123", "encoded:password123"));
    }

    @Test
    void 이미_존재하는_이메일이면_예외가_발생한다() {
        service.execute("test@test.com", "password123");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.execute("test@test.com", "password456"));

        assertEquals("이미 존재하는 이메일입니다.", exception.getMessage());
    }

    @Test
    void 비밀번호가_8자_미만이면_예외가_발생한다() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.execute("test@test.com", "short"));

        assertEquals("비밀번호는 8자 이상이어야 합니다.", exception.getMessage());
    }

    @Test
    void 비밀번호가_정확히_8자이면_성공한다() {
        String token = service.execute("test@test.com", "12345678");

        assertNotNull(token);
    }
}
