package shopping.auth;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import shopping.member.LoginMember;
import shopping.member.RegisterMember;

@SpringBootTest
class AuthenticationServiceTest {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private RegisterMember registerMember;

    @Autowired
    private LoginMember loginMember;

    @BeforeEach
    void setUp() {
        try {
            registerMember.execute("auth-test@test.com", "password123");
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Test
    void 토큰에서_회원ID를_추출한다() {
        String token = loginMember.execute("auth-test@test.com", "password123");

        UUID memberId = authenticationService.extractMemberId("Bearer " + token);

        assertNotNull(memberId);
    }

    @Test
    void 잘못된_토큰이면_예외가_발생한다() {
        assertThrows(Exception.class,
                () -> authenticationService.extractMemberId("Bearer invalid-token"));
    }
}
