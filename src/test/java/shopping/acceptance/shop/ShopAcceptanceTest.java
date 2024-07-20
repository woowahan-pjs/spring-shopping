package shopping.acceptance.shop;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shopping.acceptance.AcceptanceTest;
import shopping.acceptance.seller.steps.SellerAcceptanceSteps;
import shopping.acceptance.shop.steps.ShopAcceptanceSteps;

import static shopping.utils.fixture.SellerFixture.*;

public class ShopAcceptanceTest extends AcceptanceTest {

    @Test
    void scenario() {
    }

    @DisplayName("판매자는 로그인 되어있다면 샵을 개설할 수 있다")
    @Test
    void registerShop() {
        SellerAcceptanceSteps.회원가입되었다(EMAIL, NAME, PASSWORD, BIRTH, ADDRESS, PHONE);
        final String accessToken = SellerAcceptanceSteps.로그인되었다(EMAIL, PASSWORD);
        final ExtractableResponse<Response> response = ShopAcceptanceSteps.registerShop(accessToken, "우재샵");
        ShopAcceptanceSteps.validateRegistration(response);
    }
}
