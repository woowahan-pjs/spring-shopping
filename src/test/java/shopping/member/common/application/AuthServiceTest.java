package shopping.member.common.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import shopping.fake.FakeMemberRepository;
import shopping.fake.FakePasswordEncoder;
import shopping.fake.InMemoryMembers;
import shopping.member.client.domain.Client;
import shopping.member.common.domain.MemberRepository;
import shopping.member.common.domain.Password;
import shopping.member.common.domain.PasswordEncoder;
import shopping.member.common.exception.InvalidEmailException;
import shopping.member.common.exception.InvalidMemberException;
import shopping.member.common.exception.InvalidPasswordException;
import shopping.member.common.exception.NotFoundMemberException;

@DisplayName("AuthService")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AuthServiceTest {

    private final InMemoryMembers inMemoryMembers = new InMemoryMembers();
    private final PasswordEncoder passwordEncoder = new FakePasswordEncoder();
    private AuthService authService;

    @BeforeEach
    void setUp() {
        final MemberRepository memberRepository = new FakeMemberRepository(inMemoryMembers);
        authService = new AuthService(
                memberRepository,
                passwordEncoder,
                (email, role) -> email + " " + email
        );
    }

    @Test
    void 중복되는_이메일을_검증할_수_있다() {
        saveMember();

        assertThatThrownBy(() -> authService.validateEmail("test@test.com"))
                .isInstanceOf(InvalidEmailException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"1234", "qwer", "password"})
    void 패스워드를_암호화한다(String rawPassword) {
        final Password password = authService.encodePassword(rawPassword);

        assertThat(password.isMatch(rawPassword, passwordEncoder)).isTrue();
    }

    @Test
    void 로그인을_수행한다() {
        saveMember();

        assertThat(authService.login("test@test.com", "1234", "Client")).isNotNull();
    }

    @Test
    void 가입되지않은_멤버의_로그인을_수행하면_예외를_던진다() {
        assertThatThrownBy(() -> authService.login("test@test.com", "1234", "Client"))
                .isInstanceOf(NotFoundMemberException.class);
    }

    @Test
    void 로그인수행_시_비밀번호를_틀리면_예외를_던진다() {
        saveMember();

        assertThatThrownBy(() -> authService.login("test@test.com", "1111", "Client"))
                .isInstanceOf(InvalidPasswordException.class);
    }

    @Test
    void 로그인수행_시_권한이_없다면_예외를_던진다() {
        saveMember();

        assertThatThrownBy(() -> authService.login("test@test.com", "1234", "Owner"))
                .isInstanceOf(InvalidMemberException.class);
    }

    private void saveMember() {
        final Password password = new Password("1234", passwordEncoder);
        inMemoryMembers.save(new Client("test@test.com", password, "test"));
    }
}