package shopping.admin.application;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shopping.admin.application.command.AdminSignInCommand;
import shopping.admin.domain.Admin;
import shopping.admin.domain.AdminRepository;
import shopping.common.auth.AccessTokenRepository;
import shopping.common.exception.PasswordMissMatchException;
import shopping.utils.fake.FakeAccessTokenRepository;
import shopping.utils.fake.FakeAdminRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static shopping.utils.fixture.AdminFixture.*;

@DisplayName("어드민 로그인 테스트")
public class AdminSignInUseCaseTest {

    private AccessTokenRepository accessTokenRepository;
    private AdminRepository adminRepository;
    private AdminSignInUseCase AdminSignInUseCase;

    @BeforeEach
    void setUp() {
        adminRepository = new FakeAdminRepository();
        accessTokenRepository = new FakeAccessTokenRepository();
        AdminSignInUseCase = new AdminService(accessTokenRepository, adminRepository);
    }

    @DisplayName("회원가입이 되어있다면 이메일과 비밀번호로 로그인을 할 수 있다.")
    @Test
    void signIn() {
        adminRepository.save(new Admin(1L, NAME, EMAIL, PASSWORD));

        final AdminSignInCommand AdminSignInCommand = new AdminSignInCommand(EMAIL, PASSWORD);
        final String accessToken = AdminSignInUseCase.signIn(AdminSignInCommand);

        assertThat(accessToken).isNotBlank();
    }

    @DisplayName("회원가입이 되어있지만 이메일을 잘못 입력하면 로그인을 할 수 없다.")
    @Test
    void doNotSignInNotRegisteredEmail() {
        adminRepository.save(new Admin(1L, NAME, EMAIL, PASSWORD));
        final AdminSignInCommand AdminSignInCommand = new AdminSignInCommand(OTHER_EMAIL, PASSWORD);
        assertThatThrownBy(() -> AdminSignInUseCase.signIn(AdminSignInCommand))
                .isExactlyInstanceOf(RuntimeException.class);
    }

    @DisplayName("회원가입이 되어있지만 비밀번호를 잘못 입력하면 로그인을 할 수 없다.")
    @Test
    void doNotSignInWrongPassword() {
        adminRepository.save(new Admin(1L, NAME, EMAIL, PASSWORD));
        final AdminSignInCommand AdminSignInCommand = new AdminSignInCommand(EMAIL, OTHER_PASSWORD);
        assertThatThrownBy(() -> AdminSignInUseCase.signIn(AdminSignInCommand))
                .isExactlyInstanceOf(PasswordMissMatchException.class);
    }

    @DisplayName("비유효한 이메일을 입력하면 로그인 할 수 없다")
    @Test
    void doNotSignInInvalidEmail() {
        assertThatThrownBy(() -> new AdminSignInCommand("test@", PASSWORD))
                .isExactlyInstanceOf(ConstraintViolationException.class);
    }

    @DisplayName("비유효한 패스워드를 입력하면 로그인 할 수 없다")
    @Test
    void doNotSignInInvalidPassword() {
        assertThatThrownBy(() -> new AdminSignInCommand(EMAIL, "1234"))
                .isExactlyInstanceOf(ConstraintViolationException.class);
    }
}
