package shopping.acceptance.customer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shopping.acceptance.AcceptanceTest;
import shopping.acceptance.customer.steps.CustomerAcceptanceSteps;

import static shopping.utils.fixture.CustomerFixture.*;

@DisplayName("고객 로그인 인수 테스트")
public class SellerSignInAcceptanceTest extends AcceptanceTest {

    @Test
    void scenario() {

    }

    @DisplayName("회원가입이 되어있다면 이메일과 비밀번호로 로그인을 할 수 있다.")
    @Test
    void signIn() {
        CustomerAcceptanceSteps.signUp(EMAIL, NAME, PASSWORD, BIRTH, ADDRESS, PHONE);

        final var response = CustomerAcceptanceSteps.signIn(EMAIL, PASSWORD);
        CustomerAcceptanceSteps.validateCustomerSignIn(response);
    }

    @DisplayName("회원가입이 되어있지만 이메일을 잘못 입력하면 로그인을 할 수 없다.")
    @Test
    void doNotSignInNotRegisteredEmail() {
        CustomerAcceptanceSteps.signUp(EMAIL, NAME, PASSWORD, BIRTH, ADDRESS, PHONE);

        final var response = CustomerAcceptanceSteps.signIn(OTHER_EMAIL, PASSWORD);
        CustomerAcceptanceSteps.validateCustomerSignInNotRegisteredEmail(response);
    }

    @DisplayName("회원가입이 되어있지만 비밀번호를 잘못 입력하면 로그인을 할 수 없다.")
    @Test
    void doNotSignInWrongPassword() {
        CustomerAcceptanceSteps.signUp(EMAIL, NAME, PASSWORD, BIRTH, ADDRESS, PHONE);

        final var response = CustomerAcceptanceSteps.signIn(EMAIL, OTHER_PASSWORD);
        CustomerAcceptanceSteps.validateCustomerSignInWrongPassword(response);
    }

    @DisplayName("비유효한 이메일을 입력하면 로그인 할 수 없다")
    @Test
    void doNotSignInInvalidEmail() {
        CustomerAcceptanceSteps.signUp(EMAIL, NAME, PASSWORD, BIRTH, ADDRESS, PHONE);

        final var response = CustomerAcceptanceSteps.signIn("test@", OTHER_PASSWORD);
        CustomerAcceptanceSteps.validateCustomerSignInInvalidEmail(response);
    }

    @DisplayName("비유효한 패스워드를 입력하면 로그인 할 수 없다")
    @Test
    void doNotSignInInvalidPassword() {
        CustomerAcceptanceSteps.signUp(EMAIL, NAME, PASSWORD, BIRTH, ADDRESS, PHONE);

        final var response = CustomerAcceptanceSteps.signIn(EMAIL, "1234");
        CustomerAcceptanceSteps.validateCustomerSignInInvalidPassword(response);
    }
}
