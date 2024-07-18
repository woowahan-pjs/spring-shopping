package shopping.acceptance.seller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shopping.acceptance.AcceptanceTest;
import shopping.acceptance.seller.steps.SellerAcceptanceTest;

import static shopping.utils.fixture.SellerFixture.*;

@DisplayName("판매자 회원가입 인수 테스트")
public class SellerSignUpAcceptanceTest extends AcceptanceTest {

    @Test
    void scenario() {
    }

    @DisplayName("이메일과 비밀번호를 입력하면 회원 가입을 할 수 있다.")
    @Test
    void signUp() {
        final var response = SellerAcceptanceTest.signUp(EMAIL, NAME, PASSWORD, BIRTH, ADDRESS, PHONE);
        SellerAcceptanceTest.validateSellerSignUp(response);
    }
}
