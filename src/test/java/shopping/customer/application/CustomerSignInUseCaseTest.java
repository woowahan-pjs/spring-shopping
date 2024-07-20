package shopping.customer.application;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shopping.common.auth.AccessTokenRepository;
import shopping.common.exception.PasswordMissMatchException;
import shopping.customer.application.command.CustomerSignInCommand;
import shopping.customer.domain.CustomerSignUpRequest;
import shopping.customer.domain.repository.CustomerRepository;
import shopping.utils.fake.FakeAccessTokenRepository;
import shopping.utils.fake.FakeCustomerRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static shopping.utils.fixture.CustomerFixture.*;

@DisplayName("고객 로그인 테스트")
public class CustomerSignInUseCaseTest {

    private AccessTokenRepository accessTokenRepository;
    private CustomerRepository customerRepository;
    private CustomerSignInUseCase customerSignInUseCase;

    @BeforeEach
    void setUp() {
        accessTokenRepository = new FakeAccessTokenRepository();
        customerRepository = new FakeCustomerRepository();
        customerSignInUseCase = new CustomerSignInService(accessTokenRepository, customerRepository);
    }

    @DisplayName("회원가입이 되어있다면 이메일과 비밀번호로 로그인을 할 수 있다.")
    @Test
    void signIn() {
        customerRepository.save(new CustomerSignUpRequest(EMAIL, NAME, PASSWORD, BIRTH, ADDRESS, PHONE));

        final CustomerSignInCommand customerSignInCommand = new CustomerSignInCommand(EMAIL, PASSWORD);
        final String accessToken = customerSignInUseCase.signIn(customerSignInCommand);

        assertThat(accessToken).isNotBlank();
    }

    @DisplayName("회원가입이 되어있지만 이메일을 잘못 입력하면 로그인을 할 수 없다.")
    @Test
    void doNotSignInNotRegisteredEmail() {
        customerRepository.save(new CustomerSignUpRequest(EMAIL, NAME, PASSWORD, BIRTH, ADDRESS, PHONE));
        final CustomerSignInCommand customerSignInCommand = new CustomerSignInCommand(OTHER_EMAIL, PASSWORD);
        assertThatThrownBy(() -> customerSignInUseCase.signIn(customerSignInCommand))
                .isExactlyInstanceOf(RuntimeException.class);
    }

    @DisplayName("회원가입이 되어있지만 비밀번호를 잘못 입력하면 로그인을 할 수 없다.")
    @Test
    void doNotSignInWrongPassword() {
        customerRepository.save(new CustomerSignUpRequest(EMAIL, NAME, PASSWORD, BIRTH, ADDRESS, PHONE));
        final CustomerSignInCommand customerSignInCommand = new CustomerSignInCommand(EMAIL, OTHER_PASSWORD);
        assertThatThrownBy(() -> customerSignInUseCase.signIn(customerSignInCommand))
                .isExactlyInstanceOf(PasswordMissMatchException.class);
    }

    @DisplayName("비유효한 이메일을 입력하면 로그인 할 수 없다")
    @Test
    void doNotSignInInvalidEmail() {
        assertThatThrownBy(() -> new CustomerSignInCommand("test@", PASSWORD))
                .isExactlyInstanceOf(ConstraintViolationException.class);
    }

    @DisplayName("비유효한 패스워드를 입력하면 로그인 할 수 없다")
    @Test
    void doNotSignInInvalidPassword() {
        assertThatThrownBy(() -> new CustomerSignInCommand(EMAIL, "1234"))
                .isExactlyInstanceOf(ConstraintViolationException.class);
    }
}
