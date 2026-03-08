package shopping.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shopping.domain.InMemoryMemberRepository;
import shopping.domain.Member;

import static org.assertj.core.api.Assertions.*;

class MemberServiceTest {
    private MemberService service;

    @BeforeEach
    void setUp() {
        service = new MemberService(new InMemoryMemberRepository(), new RegexEmailFormatValidator());
    }

    @Test
    @DisplayName("회원가입을 한다.")
    void register() {
        Member member = new Member("test@gmail.com", "password");

        service.register(member);

        assertThat(member.getId()).isNotNull();
    }

    @Test
    @DisplayName("잘못된 이메일은 예외가 발생한다.")
    void invalidEmail() {
        Member member = new Member("test", "password");

        assertThatThrownBy(() -> service.register(member))
                .isInstanceOf(IllegalArgumentException.class);
    }
}