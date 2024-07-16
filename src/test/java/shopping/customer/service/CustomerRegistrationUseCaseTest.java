package shopping.customer.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("고객 가입 테스트")
public class CustomerRegistrationUseCaseTest {

    public static final String EMAIL = "test@email.com";
    public static final String NAME = "JOHN DOE";
    public static final String PASSWORD = "hell!wor2dssfF";
    public static final String BIRTH = "19960911";
    public static final String ADDRESS = "1234 Elm Street, Springfield, IL 62704";
    public static final String PHONE = "555-123-4567";
    public static final String OTHER_NAME = "FOO BAR";
    public static final String OTHER_PASSWORD = "hell!woR2dssf";
    public static final String OTHER_BIRTH = "19970204";
    public static final String OTHER_ADDRESS = "1235 Elvis Street, GreenHil, IL 21704";
    public static final String OTHER_PHONE = "520-124-3567";

    private CustomerRepository customerRepository;
    private CustomerRegistrationUseCase customerRegistrationUseCase;

    @BeforeEach
    void setUp() {
        customerRepository = new FakeCustomerRepository();
        customerRegistrationUseCase = new CustomerService(customerRepository);
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
}

interface CustomerRegistrationUseCase {
    Customer register(final CustomerCommand customerCommand);
}

interface CustomerRepository {
    Customer save(CustomerRegistration customerRegistration);
}

class FakeCustomerRepository implements CustomerRepository {
    private final Map<Long, Customer> storage = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    @Override
    public Customer save(final CustomerRegistration customerRegistration) {
        final boolean exist = storage.values().stream()
                .map(Customer::getEmail)
                .anyMatch(it -> it.equals(customerRegistration.getEmail()));
        if (exist) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        final var id = idGenerator.incrementAndGet();
        final Customer customer = new Customer(
                id,
                customerRegistration.getEmail(),
                customerRegistration.getName(),
                customerRegistration.getPassword(),
                customerRegistration.getBirth(),
                customerRegistration.getAddress(),
                customerRegistration.getPhone()
        );
        storage.put(id, customer);
        return customer;
    }
}


class CustomerService implements CustomerRegistrationUseCase {

    private final CustomerRepository customerRepository;

    public CustomerService(final CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer register(final CustomerCommand customerCommand) {
        return customerRepository.save(customerCommand.toDomain());
    }
}

interface CommandValidating<T> {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    default void validateSelf(T t) {
        final Set<ConstraintViolation<T>> violations = validator.validate(t);
        if (violations.isEmpty()) {
            return;
        }
        throw new ConstraintViolationException(violations);
    }
}


class CustomerCommand implements CommandValidating {
    @Email
    private final String email;
    @NotBlank
    private final String name;
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*]).{12,}$")
    private final String password;
    private final String birth;
    private final String address;
    private final String phone;

    CustomerCommand(final String email, final String name, final String password, final String birth, final String address, final String phone) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.birth = birth;
        this.address = address;
        this.phone = phone;
        validateSelf(this);
    }

    public CustomerRegistration toDomain() {
        return new CustomerRegistration(
                this.email,
                this.name,
                this.password,
                this.birth,
                this.address, this.phone);
    }
}

class CustomerRegistration {
    private final String email;
    private final String name;
    private final String password;
    private final String birth;
    private final String address;
    private final String phone;

    CustomerRegistration(final String email, final String name, final String password, final String birth, final String address, final String phone) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.birth = birth;
        this.address = address;
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getBirth() {
        return birth;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }
}

class Customer {
    private final long id;
    private final String email;
    private final String name;
    private final String password;
    private final String birth;
    private final String address;
    private final String phone;

    public Customer(final long id, final String email, final String name, final String password, final String birth, final String address, final String phone) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.birth = birth;
        this.address = address;
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getBirth() {
        return birth;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }
}

