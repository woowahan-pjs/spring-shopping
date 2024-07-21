package shopping.acceptance.admin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shopping.acceptance.AcceptanceTest;
import shopping.acceptance.admin.steps.AdminAcceptanceSteps;

import static shopping.utils.fixture.AdminFixture.*;

@DisplayName("관리자 인수 테스트")
public class AdminAcceptanceTest extends AcceptanceTest {

    @Test
    void scenario() {
    }

    @DisplayName("이메일과 비밀번호를 입력하면 회원 가입을 할 수 있다.")
    @Test
    void signUp() {
        final var response = AdminAcceptanceSteps.signUp(EMAIL, NAME, PASSWORD);
        AdminAcceptanceSteps.validateSellerSignUp(response);
    }

    @DisplayName("비유효한 이메일을 입력하면 회원 가입 할 수 없다")
    @Test
    void doNotSignUpInvalidEmail() {
        final var response = AdminAcceptanceSteps.signUp("test@", NAME, PASSWORD);
        AdminAcceptanceSteps.validateCustomerSignUpInvalidEmail(response);
    }

    @DisplayName("비유효한 패스워드를 입력하면 회원 가입 할 수 없다")
    @Test
    void doNotSignUpInvalidPassword() {
        final var response = AdminAcceptanceSteps.signUp(EMAIL, NAME, "1234");
        AdminAcceptanceSteps.validateCustomerSignUpInvalidPassword(response);
    }

    @DisplayName("회원가입이 되어있다면 이메일과 비밀번호로 로그인을 할 수 있다.")
    @Test
    void signIn() {
        AdminAcceptanceSteps.signUp(EMAIL, NAME, PASSWORD);

        final var response = AdminAcceptanceSteps.signIn(EMAIL, PASSWORD);
        AdminAcceptanceSteps.validateSellerSignIn(response);
    }

    @DisplayName("회원가입이 되어있지만 이메일을 잘못 입력하면 로그인을 할 수 없다.")
    @Test
    void doNotSignInNotRegisteredEmail() {
        AdminAcceptanceSteps.signUp(EMAIL, NAME, PASSWORD);

        final var response = AdminAcceptanceSteps.signIn(OTHER_EMAIL, PASSWORD);
        AdminAcceptanceSteps.validateSellerSignInNotRegisteredEmail(response);
    }

    @DisplayName("회원가입이 되어있지만 비밀번호를 잘못 입력하면 로그인을 할 수 없다.")
    @Test
    void doNotSignInWrongPassword() {
        AdminAcceptanceSteps.signUp(EMAIL, NAME, PASSWORD);

        final var response = AdminAcceptanceSteps.signIn(EMAIL, OTHER_PASSWORD);
        AdminAcceptanceSteps.validateSellerSignInWrongPassword(response);
    }

    @DisplayName("비유효한 이메일을 입력하면 로그인 할 수 없다")
    @Test
    void doNotSignInInvalidEmail() {
        AdminAcceptanceSteps.signUp(EMAIL, NAME, PASSWORD);

        final var response = AdminAcceptanceSteps.signIn("test@", OTHER_PASSWORD);
        AdminAcceptanceSteps.validateSellerSignInInvalidEmail(response);
    }

    @DisplayName("비유효한 패스워드를 입력하면 로그인 할 수 없다")
    @Test
    void doNotSignInInvalidPassword() {
        AdminAcceptanceSteps.signUp(EMAIL, NAME, PASSWORD);

        final var response = AdminAcceptanceSteps.signIn(EMAIL, "1234");
        AdminAcceptanceSteps.validateSellerSignInInvalidPassword(response);
    }
}
