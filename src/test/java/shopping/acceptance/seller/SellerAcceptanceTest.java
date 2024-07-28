package shopping.acceptance.seller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shopping.acceptance.AcceptanceTest;
import shopping.acceptance.seller.steps.SellerAcceptanceSteps;

import static shopping.utils.fixture.CustomerFixture.ADDRESS;
import static shopping.utils.fixture.CustomerFixture.BIRTH;
import static shopping.utils.fixture.CustomerFixture.EMAIL;
import static shopping.utils.fixture.CustomerFixture.NAME;
import static shopping.utils.fixture.CustomerFixture.PASSWORD;
import static shopping.utils.fixture.CustomerFixture.PHONE;
import static shopping.utils.fixture.SellerFixture.*;

@DisplayName("판매자 인수 테스트")
public class SellerAcceptanceTest extends AcceptanceTest {

    @Test
    void scenario() {

    }
    @DisplayName("이메일과 비밀번호를 입력하면 회원 가입을 할 수 있다.")
    @Test
    void signUp() {
        final var response = SellerAcceptanceSteps.signUp(EMAIL, NAME, PASSWORD, BIRTH, ADDRESS, PHONE);
        SellerAcceptanceSteps.validateSellerSignUp(response);
    }

    @DisplayName("비유효한 이메일을 입력하면 회원 가입 할 수 없다")
    @Test
    void doNotSignUpInvalidEmail() {
        final var response = SellerAcceptanceSteps.signUp("test@", NAME, PASSWORD, BIRTH, ADDRESS, PHONE);
        SellerAcceptanceSteps.validateCustomerSignUpInvalidEmail(response);
    }

    @DisplayName("비유효한 패스워드를 입력하면 회원 가입 할 수 없다")
    @Test
    void doNotSignUpInvalidPassword() {
        final var response = SellerAcceptanceSteps.signUp(EMAIL, NAME, "1234", BIRTH, ADDRESS, PHONE);
        SellerAcceptanceSteps.validateCustomerSignUpInvalidPassword(response);
    }

    @DisplayName("회원가입이 되어있다면 이메일과 비밀번호로 로그인을 할 수 있다.")
    @Test
    void signIn() {
        SellerAcceptanceSteps.signUp(EMAIL, NAME, PASSWORD, BIRTH, ADDRESS, PHONE);

        final var response = SellerAcceptanceSteps.signIn(EMAIL, PASSWORD);
        SellerAcceptanceSteps.validateSellerSignIn(response);
    }

    @DisplayName("회원가입이 되어있지만 이메일을 잘못 입력하면 로그인을 할 수 없다.")
    @Test
    void doNotSignInNotRegisteredEmail() {
        SellerAcceptanceSteps.signUp(EMAIL, NAME, PASSWORD, BIRTH, ADDRESS, PHONE);

        final var response = SellerAcceptanceSteps.signIn(OTHER_EMAIL, PASSWORD);
        SellerAcceptanceSteps.validateSellerSignInNotRegisteredEmail(response);
    }

    @DisplayName("회원가입이 되어있지만 비밀번호를 잘못 입력하면 로그인을 할 수 없다.")
    @Test
    void doNotSignInWrongPassword() {
        SellerAcceptanceSteps.signUp(EMAIL, NAME, PASSWORD, BIRTH, ADDRESS, PHONE);

        final var response = SellerAcceptanceSteps.signIn(EMAIL, OTHER_PASSWORD);
        SellerAcceptanceSteps.validateSellerSignInWrongPassword(response);
    }

    @DisplayName("비유효한 이메일을 입력하면 로그인 할 수 없다")
    @Test
    void doNotSignInInvalidEmail() {
        SellerAcceptanceSteps.signUp(EMAIL, NAME, PASSWORD, BIRTH, ADDRESS, PHONE);

        final var response = SellerAcceptanceSteps.signIn("test@", OTHER_PASSWORD);
        SellerAcceptanceSteps.validateSellerSignInInvalidEmail(response);
    }

    @DisplayName("비유효한 패스워드를 입력하면 로그인 할 수 없다")
    @Test
    void doNotSignInInvalidPassword() {
        SellerAcceptanceSteps.signUp(EMAIL, NAME, PASSWORD, BIRTH, ADDRESS, PHONE);

        final var response = SellerAcceptanceSteps.signIn(EMAIL, "1234");
        SellerAcceptanceSteps.validateSellerSignInInvalidPassword(response);
    }

    @DisplayName("로그인 되어있다면 내 정보를 확인할 수 있다")
    @Test
    void getSellerInfo() {
        SellerAcceptanceSteps.signUp(EMAIL, NAME, PASSWORD, BIRTH, ADDRESS, PHONE);
        final String accessToken = SellerAcceptanceSteps.로그인됨(EMAIL, PASSWORD);

        final var response = SellerAcceptanceSteps.getSellerInfo(accessToken);
        SellerAcceptanceSteps.validateSellerInfo(response, NAME);
    }
}
