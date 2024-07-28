package shopping.admin.application;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shopping.admin.application.command.AdminSignUpCommand;
import shopping.admin.domain.Admin;
import shopping.admin.domain.AdminRepository;
import shopping.common.auth.AccessTokenRepository;
import shopping.utils.fake.FakeAccessTokenRepository;
import shopping.utils.fake.FakeAdminRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static shopping.utils.fixture.AdminFixture.*;

@DisplayName("어드민 회원가입 테스트")
public class AdminSignUpUseCaseTest {

    private AdminRepository adminRepository;
    private AccessTokenRepository accessTokenRepository;
    private AdminSignUpUseCase AdminSignUpUseCase;

    @BeforeEach
    void setUp() {
        adminRepository = new FakeAdminRepository();
        accessTokenRepository = new FakeAccessTokenRepository();
        AdminSignUpUseCase = new AdminService(accessTokenRepository, adminRepository);
    }

    @DisplayName("미가입 어드민은 이메일과 비밀번호를 입력하면 회원 가입 할 수 있다.")
    @Test
    void signUp() {
        final AdminSignUpCommand AdminSignUpCommand = new AdminSignUpCommand(EMAIL, NAME, PASSWORD);
        final Admin admin = AdminSignUpUseCase.signUp(AdminSignUpCommand);

        assertAll(
                () -> assertNotNull(admin),
                () -> assertThat(admin.email()).isEqualTo(EMAIL),
                () -> assertThat(admin.name()).isEqualTo(NAME),
                () -> assertThat(admin.password()).isEqualTo(PASSWORD)
        );
    }

    @DisplayName("비유효한 이메일을 입력하면 회원 가입 할 수 없다")
    @Test
    void doNotSignUpInvalidEmail() {
        assertThatThrownBy(() -> new AdminSignUpCommand("test@", NAME, PASSWORD))
                .isExactlyInstanceOf(ConstraintViolationException.class);
    }

    @DisplayName("비유효한 패스워드를 입력하면 회원 가입 할 수 없다")
    @Test
    void doNotSignUpInvalidPassword() {
        assertThatThrownBy(() -> new AdminSignUpCommand(EMAIL, NAME, "1234"))
                .isExactlyInstanceOf(ConstraintViolationException.class);
    }

    @DisplayName("이미 존재하는 이메일을 입력하면 회원 가입 할 수 없다")
    @Test
    void doNotSignUpDuplicatedEmail() {
        adminRepository.save(new Admin(null, EMAIL, NAME, PASSWORD));
        assertThatThrownBy(() -> AdminSignUpUseCase.signUp(new AdminSignUpCommand(EMAIL, OTHER_NAME, OTHER_PASSWORD)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
