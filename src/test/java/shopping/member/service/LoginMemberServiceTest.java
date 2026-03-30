package shopping.member.service;

import shopping.member.domain.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoginMemberServiceTest {

    private InMemoryMemberRepository memberRepository;
    private FakeTokenProvider tokenProvider;
    private FakePasswordEncoder passwordEncoder;
    private PasswordFactory passwordFactory;
    private LoginMemberService service;

    @BeforeEach
    void setUp() {
        memberRepository = new InMemoryMemberRepository();
        tokenProvider = new FakeTokenProvider();
        passwordEncoder = new FakePasswordEncoder();
        passwordFactory = new PasswordFactory(passwordEncoder);
        service = new LoginMemberService(memberRepository, tokenProvider, passwordEncoder);
    }

    @Test
    void 로그인에_성공하면_토큰을_반환한다() {
        Member member = memberRepository
                .save(new Member("test@test.com", passwordFactory.create("password123")));

        String token = service.execute("test@test.com", "password123");

        assertEquals("token:" + member.getId(), token);
    }

    @Test
    void 존재하지_않는_이메일이면_예외가_발생한다() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.execute("unknown@test.com", "password123"));

        assertEquals("이메일 또는 비밀번호가 잘못되었습니다.", exception.getMessage());
    }

    @Test
    void 비밀번호가_틀리면_예외가_발생한다() {
        memberRepository.save(new Member("test@test.com", passwordFactory.create("password123")));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.execute("test@test.com", "wrongpassword"));

        assertEquals("이메일 또는 비밀번호가 잘못되었습니다.", exception.getMessage());
    }
}
