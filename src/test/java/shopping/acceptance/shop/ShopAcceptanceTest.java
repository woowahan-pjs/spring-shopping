package shopping.acceptance.shop;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shopping.acceptance.AcceptanceTest;
import shopping.acceptance.customer.steps.CustomerAcceptanceSteps;
import shopping.acceptance.seller.steps.SellerAcceptanceSteps;
import shopping.acceptance.shop.steps.ShopAcceptanceSteps;
import shopping.utils.fixture.ShopFixture;

import static shopping.utils.fixture.SellerFixture.*;

@DisplayName("상점 인수 테스트")
public class ShopAcceptanceTest extends AcceptanceTest {

    @Test
    void scenario() {
    }

    @DisplayName("판매자는 로그인 되어있다면 샵을 개설할 수 있다")
    @Test
    void registerShop() {
        SellerAcceptanceSteps.회원가입됨(EMAIL, NAME, PASSWORD, BIRTH, ADDRESS, PHONE);
        final String accessToken = SellerAcceptanceSteps.로그인됨(EMAIL, PASSWORD);
        final ExtractableResponse<Response> response = ShopAcceptanceSteps.registerShop(accessToken, ShopFixture.NAME);
        ShopAcceptanceSteps.validateRegistration(response);
    }

    @DisplayName("고객은 로그인 되어있어도 샵을 개설할 수 없다")
    @Test
    void registerShopInvalidCustomer() {
        CustomerAcceptanceSteps.회원가입됨(EMAIL, NAME, PASSWORD, BIRTH, ADDRESS, PHONE);
        final String accessToken = CustomerAcceptanceSteps.로그인됨(EMAIL, PASSWORD);
        final ExtractableResponse<Response> response = ShopAcceptanceSteps.registerShop(accessToken, ShopFixture.NAME);
        ShopAcceptanceSteps.validateRegistrationInvalidCustomer(response);
    }
}
