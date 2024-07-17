package shopping.acceptance.customer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shopping.acceptance.AcceptanceTest;
import shopping.acceptance.customer.steps.CustomerAcceptanceSteps;

import static shopping.utils.fixture.CustomerFixture.*;

@DisplayName("일반 사용자 로그인 인수 테스트")
public class CustomerSignInAcceptanceTest extends AcceptanceTest {

    @Test
    void scenario() {

    }

    @DisplayName("일반 사용자는 회원 가입이 되어있다면 이메일과 비밀번호로 로그인을 할 수 있다.")
    @Test
    void signIn() {
        CustomerAcceptanceSteps.signUp(EMAIL, NAME, PASSWORD, BIRTH, ADDRESS, PHONE);

        final var response = CustomerAcceptanceSteps.signIn(EMAIL, PASSWORD);
        CustomerAcceptanceSteps.validateCustomerSignIn(response);
    }
}
