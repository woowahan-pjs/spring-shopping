package shopping.member.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shopping.member.domain.InMemoryMemberRepository;
import shopping.member.domain.Member;

import static org.assertj.core.api.Assertions.*;

@DisplayName("회원 서비스")
class MemberServiceTest {
    private MemberService service;

    @BeforeEach
    void setUp() {
        service = new MemberService(new InMemoryMemberRepository(), new RegexEmailFormatValidator(), new FakePasswordEncryptor());
    }

    @Test
    @DisplayName("회원가입을 한다.")
    void register() {
        Member member = new Member("test@gmail.com", "password");

        service.register(member);

        assertThat(member.getId()).isNotNull();
    }

    @Test
    @DisplayName("존재하는 이메일은 예외가 발생.")
    void invalidDuplicateEmail() {
        Member member = new Member("test@gmail.com", "password");

        service.register(member);

        assertThatThrownBy(() -> service.register(member))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("잘못된 이메일은 예외가 발생한다.")
    void invalidEmail() {
        Member member = new Member("test", "password");

        assertThatThrownBy(() -> service.register(member))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("회원이 로그인한다.")
    void login() {
        String email = "test@gmail.com";
        String password = "password";
        Member member = new Member(email, password);
        service.register(member);

        Member found = service.login(member.getEmail(), password);

        assertThat(found.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("로그인 시 이메일이 일치하지 않으면 예외가 발생한다")
    void invalidEmailLogin() {
        String password = "password";
        Member member = new Member("test@gmail.com", password);
        service.register(member);

        assertThatThrownBy(() -> service.login("test@naver.com", password))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("로그인 시 패스워드가 일치하지 않으면 예외가 발생한다")
    void invalidPasswordLogin() {
        String email = "test@gmail.com";
        Member member = new Member(email, "password");
        service.register(member);

        assertThatThrownBy(() -> service.login(email, "pass"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}