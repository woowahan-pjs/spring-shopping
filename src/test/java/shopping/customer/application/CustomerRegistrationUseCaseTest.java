package shopping.customer.application;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shopping.customer.domain.Customer;
import shopping.customer.domain.CustomerRegistration;
import shopping.customer.domain.repository.CustomerRepository;
import shopping.customer.application.command.CustomerCommand;
import shopping.utils.fake.FakeCustomerRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static shopping.utils.fixture.CustomerFixture.*;

@DisplayName("고객 가입 테스트")
public class CustomerRegistrationUseCaseTest {

    private CustomerRepository customerRepository;
    private CustomerRegistrationUseCase customerRegistrationUseCase;

    @BeforeEach
    void setUp() {
        customerRepository = new FakeCustomerRepository();
        customerRegistrationUseCase = new CustomerRegistrationService(customerRepository);
    }

    @DisplayName("미가입 고객은 이메일과 비밀번호를 입력하면 회원 가입 할 수 있다.")
    @Test
    void register() {
        final CustomerCommand customerCommand = new CustomerCommand(EMAIL, NAME, PASSWORD, BIRTH, ADDRESS, PHONE);
        final Customer customer = customerRegistrationUseCase.register(customerCommand);

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
    void doNotRegisterInvalidEmail() {
        assertThatThrownBy(() -> new CustomerCommand("test@", NAME, PASSWORD, BIRTH, ADDRESS, PHONE))
                .isExactlyInstanceOf(ConstraintViolationException.class);
    }

    @DisplayName("비유효한 패스워드를 입력하면 회원 가입 할 수 없다")
    @Test
    void doNotRegisterInvalidPassword() {
        assertThatThrownBy(() -> new CustomerCommand(EMAIL, NAME, "1234", BIRTH, ADDRESS, PHONE))
                .isExactlyInstanceOf(ConstraintViolationException.class);
    }

    @DisplayName("이미 존재하는 이메일을 입력하면 회원 가입 할 수 없다")
    @Test
    void doNotRegisterDuplicatedEmail() {
        customerRepository.save(new CustomerRegistration(EMAIL, NAME, PASSWORD, BIRTH, ADDRESS, PHONE));
        assertThatThrownBy(() -> customerRegistrationUseCase.register(new CustomerCommand(EMAIL, OTHER_NAME, OTHER_PASSWORD, OTHER_BIRTH, OTHER_ADDRESS, OTHER_PHONE)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
