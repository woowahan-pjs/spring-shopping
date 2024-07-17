package shopping.customer.application;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shopping.customer.domain.Customer;
import shopping.customer.domain.CustomerSignUpRequest;
import shopping.customer.domain.repository.CustomerRepository;
import shopping.customer.application.command.CustomerSignUpCommand;
import shopping.utils.fake.FakeCustomerRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static shopping.utils.fixture.CustomerFixture.*;

@DisplayName("고객 회원가입 테스트")
public class CustomerSignUpUseCaseTest {

    private CustomerRepository customerRepository;
    private CustomerSignUpUseCase customerSignUpUseCase;

    @BeforeEach
    void setUp() {
        customerRepository = new FakeCustomerRepository();
        customerSignUpUseCase = new CustomerSignUpService(customerRepository);
    }

    @DisplayName("미가입 고객은 이메일과 비밀번호를 입력하면 회원 가입 할 수 있다.")
    @Test
    void signUp() {
        final CustomerSignUpCommand customerSignUpCommand = new CustomerSignUpCommand(EMAIL, NAME, PASSWORD, BIRTH, ADDRESS, PHONE);
        final Customer customer = customerSignUpUseCase.signUp(customerSignUpCommand);

        assertAll(
                () -> assertNotNull(customer),
                () -> assertThat(customer.getEmail()).isEqualTo(EMAIL),
                () -> assertThat(customer.getName()).isEqualTo(NAME),
                () -> assertThat(customer.getPassword()).isEqualTo(PASSWORD),
                () -> assertThat(customer.getBirth()).isEqualTo(BIRTH),
                () -> assertThat(customer.getAddress()).isEqualTo(ADDRESS),
                () -> assertThat(customer.getPhone()).isEqualTo(PHONE)
        );
    }

    @DisplayName("비유효한 이메일을 입력하면 회원 가입 할 수 없다")
    @Test
    void doNotSignUpInvalidEmail() {
        assertThatThrownBy(() -> new CustomerSignUpCommand("test@", NAME, PASSWORD, BIRTH, ADDRESS, PHONE))
                .isExactlyInstanceOf(ConstraintViolationException.class);
    }

    @DisplayName("비유효한 패스워드를 입력하면 회원 가입 할 수 없다")
    @Test
    void doNotSignUpInvalidPassword() {
        assertThatThrownBy(() -> new CustomerSignUpCommand(EMAIL, NAME, "1234", BIRTH, ADDRESS, PHONE))
                .isExactlyInstanceOf(ConstraintViolationException.class);
    }

    @DisplayName("이미 존재하는 이메일을 입력하면 회원 가입 할 수 없다")
    @Test
    void doNotSignUpDuplicatedEmail() {
        customerRepository.save(new CustomerSignUpRequest(EMAIL, NAME, PASSWORD, BIRTH, ADDRESS, PHONE));
        assertThatThrownBy(() -> customerSignUpUseCase.signUp(new CustomerSignUpCommand(EMAIL, OTHER_NAME, OTHER_PASSWORD, OTHER_BIRTH, OTHER_ADDRESS, OTHER_PHONE)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
