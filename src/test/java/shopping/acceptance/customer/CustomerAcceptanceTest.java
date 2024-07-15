package shopping.acceptance.customer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shopping.acceptance.AcceptanceTest;
import shopping.acceptance.customer.steps.CustomerAcceptanceSteps;

@DisplayName("일반 사용자 인수 테스트")
public class CustomerAcceptanceTest extends AcceptanceTest {

    @Test
    void scenario() {

    }

    @DisplayName("이메일과 비밀번호를 입력하면 회원 가입을 할 수 있다.")
    @Test
    void createUser() {
        final var response = CustomerAcceptanceSteps.createCustomer();
        CustomerAcceptanceSteps.validateCreateCustomer(response);
    }
}
