package shopping.acceptance.customer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shopping.acceptance.AcceptanceTest;
import shopping.acceptance.customer.steps.CustomerAcceptanceSteps;

import static shopping.utils.fixture.CustomerFixture.*;

@DisplayName("고객 인수 테스트")
public class CustomerAcceptanceTest extends AcceptanceTest {

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

    @DisplayName("로그인 되어있다면 내 정보를 확인할 수 있다")
    @Test
    void getCustomerInfo() {
        CustomerAcceptanceSteps.signUp(EMAIL, NAME, PASSWORD, BIRTH, ADDRESS, PHONE);
        final String accessToken = CustomerAcceptanceSteps.로그인됨(EMAIL, PASSWORD);

        final var response = CustomerAcceptanceSteps.getCustomerInfo(accessToken);
        CustomerAcceptanceSteps.validateCustomerInfo(response, NAME);
    }

    @DisplayName("로그인 되어있다면 로그아웃을 할 수 있다")
    @Test
    void signOut() {
        CustomerAcceptanceSteps.signUp(EMAIL, NAME, PASSWORD, BIRTH, ADDRESS, PHONE);
        final String accessToken = CustomerAcceptanceSteps.로그인됨(EMAIL, PASSWORD);

        final var response = CustomerAcceptanceSteps.signOut(accessToken);
        CustomerAcceptanceSteps.validateSignOut(response);
    }
}
