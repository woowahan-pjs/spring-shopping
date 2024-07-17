package shopping.acceptance.customer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shopping.acceptance.AcceptanceTest;
import shopping.acceptance.customer.steps.CustomerAcceptanceSteps;

import static shopping.utils.fixture.CustomerFixture.*;
import static shopping.utils.fixture.CustomerFixture.PHONE;

@DisplayName("일반 사용자 인수 테스트")
public class CustomerSignUpAcceptanceTest extends AcceptanceTest {

    @Test
    void scenario() {

    }

    @DisplayName("이메일과 비밀번호를 입력하면 회원 가입을 할 수 있다.")
    @Test
    void signUp() {
        final var response = CustomerAcceptanceSteps.signUp(EMAIL, NAME, PASSWORD, BIRTH, ADDRESS, PHONE);
        CustomerAcceptanceSteps.validateCustomerSignUp(response);
    }

    @DisplayName("비유효한 이메일을 입력하면 회원 가입 할 수 없다")
    @Test
    void doNotSignUpInvalidEmail() {
        final var response = CustomerAcceptanceSteps.signUp("test@", NAME, PASSWORD, BIRTH, ADDRESS, PHONE);
        CustomerAcceptanceSteps.validateCustomerSignUpInvalidEmail(response);
    }

    @DisplayName("비유효한 패스워드를 입력하면 회원 가입 할 수 없다")
    @Test
    void doNotSignUpInvalidPassword() {
        final var response = CustomerAcceptanceSteps.signUp(EMAIL, NAME, "1234", BIRTH, ADDRESS, PHONE);
        CustomerAcceptanceSteps.validateCustomerSignUpInvalidPassword(response);
    }

    @DisplayName("이미 존재하는 이메일을 입력하면 회원 가입 할 수 없다")
    @Test
    void doNotSignUpDuplicatedEmail() {
        final var response = CustomerAcceptanceSteps.signUp(EMAIL, NAME, PASSWORD, BIRTH, ADDRESS, PHONE);
        CustomerAcceptanceSteps.validateCustomerSignUp(response);

        final var secondResponse = CustomerAcceptanceSteps.signUp(EMAIL, OTHER_NAME, OTHER_PASSWORD, OTHER_BIRTH, OTHER_ADDRESS, OTHER_PHONE);
        CustomerAcceptanceSteps.validateCustomerSignUpDuplicatedEmail(secondResponse);
    }
}
