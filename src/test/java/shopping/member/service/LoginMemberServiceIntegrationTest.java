package shopping.member.service;

import shopping.member.domain.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LoginMemberServiceIntegrationTest {

    @Autowired
    private RegisterMember registerMember;

    @Autowired
    private LoginMember loginMember;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        try {
            registerMember.execute("login-test@test.com", "password123");
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Test
    void 로그인에_성공하면_토큰을_반환한다() {
        String token = loginMember.execute("login-test@test.com", "password123");

        assertNotNull(token);
        UUID memberId = tokenProvider.extractMemberId(token);
        Member member = memberRepository.findByEmail("login-test@test.com").orElseThrow();
        assertEquals(member.getId(), memberId);
    }

    @Test
    void 존재하지_않는_이메일이면_예외가_발생한다() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> loginMember.execute("unknown@test.com", "password123"));

        assertEquals("이메일 또는 비밀번호가 잘못되었습니다.", exception.getMessage());
    }

    @Test
    void 비밀번호가_틀리면_예외가_발생한다() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> loginMember.execute("login-test@test.com", "wrongpassword"));

        assertEquals("이메일 또는 비밀번호가 잘못되었습니다.", exception.getMessage());
    }
}
