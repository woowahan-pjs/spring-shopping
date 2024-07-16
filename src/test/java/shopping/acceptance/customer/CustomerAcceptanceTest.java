package shopping.acceptance.customer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shopping.acceptance.AcceptanceTest;
import shopping.acceptance.customer.steps.CustomerAcceptanceSteps;

import static shopping.utils.fixture.CustomerFixture.*;
import static shopping.utils.fixture.CustomerFixture.PHONE;

@DisplayName("일반 사용자 인수 테스트")
public class CustomerAcceptanceTest extends AcceptanceTest {

    @Test
    void scenario() {

    }

    @DisplayName("이메일과 비밀번호를 입력하면 회원 가입을 할 수 있다.")
    @Test
    void register() {
        final var response = CustomerAcceptanceSteps.register(EMAIL, NAME, PASSWORD, BIRTH, ADDRESS, PHONE);
        CustomerAcceptanceSteps.validateCustomerRegistration(response);
    }

    @DisplayName("비유효한 이메일을 입력하면 회원 가입 할 수 없다")
    @Test
    void doNotRegisterInvalidEmail() {
        final var response = CustomerAcceptanceSteps.register("test@", NAME, PASSWORD, BIRTH, ADDRESS, PHONE);
        CustomerAcceptanceSteps.validateCustomerRegistrationInvalidEmail(response);
    }

    @DisplayName("비유효한 패스워드를 입력하면 회원 가입 할 수 없다")
    @Test
    void doNotRegisterInvalidPassword() {
        final var response = CustomerAcceptanceSteps.register(EMAIL, NAME, "1234", BIRTH, ADDRESS, PHONE);
        CustomerAcceptanceSteps.validateCustomerRegistrationInvalidPassword(response);
    }
}
