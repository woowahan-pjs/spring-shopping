package shopping.acceptance.product;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shopping.acceptance.AcceptanceTest;
import shopping.acceptance.admin.steps.AdminAcceptanceSteps;
import shopping.acceptance.category.steps.CategoryAcceptanceSteps;
import shopping.acceptance.product.steps.ProductAcceptanceSteps;
import shopping.acceptance.seller.steps.SellerAcceptanceSteps;
import shopping.acceptance.shop.steps.ShopAcceptanceSteps;
import shopping.utils.fixture.AdminFixture;
import shopping.utils.fixture.CategoryFixture;
import shopping.utils.fixture.SellerFixture;
import shopping.utils.fixture.ShopFixture;

import static shopping.utils.fixture.ProductFixture.*;

public class ProductAcceptanceTest extends AcceptanceTest {

    @DisplayName("판매자는 상점에 상품을 등록할 수 있다")
    @Test
    void registerProduct() {
        AdminAcceptanceSteps.회원가입됨(AdminFixture.EMAIL, AdminFixture.NAME, AdminFixture.PASSWORD);
        final String adminAccessToken = AdminAcceptanceSteps.로그인됨(AdminFixture.EMAIL, AdminFixture.PASSWORD);
        final long 메인카테고리 = CategoryAcceptanceSteps.메인카테고리생성됨(CategoryFixture.NAME, CategoryFixture.ORDER, adminAccessToken);
        final long 서브카테고리 = CategoryAcceptanceSteps.서브카테고리생성됨(CategoryFixture.SUB_NAME, CategoryFixture.SUB_ORDER, 메인카테고리, adminAccessToken);

        SellerAcceptanceSteps.회원가입됨(SellerFixture.EMAIL, SellerFixture.NAME, SellerFixture.PASSWORD, SellerFixture.BIRTH, SellerFixture.ADDRESS, SellerFixture.PHONE);
        final String sellerAccessToken = SellerAcceptanceSteps.로그인됨(SellerFixture.EMAIL, SellerFixture.PASSWORD);
        final long 상점 = ShopAcceptanceSteps.상점생성됨(sellerAccessToken, ShopFixture.NAME);

        final ExtractableResponse<Response> responseExtractableResponse = ProductAcceptanceSteps.registerProduct(상점, 서브카테고리, NAME, AMOUNT, IMAGE_URL, sellerAccessToken);
        ProductAcceptanceSteps.validate(responseExtractableResponse);
    }

    @DisplayName("상품 이름이 공백 포함 15글자 넘어간다면 상품을 등록할 수 없다")
    @Test
    void registerProductInvalidNameLength() {
        AdminAcceptanceSteps.회원가입됨(AdminFixture.EMAIL, AdminFixture.NAME, AdminFixture.PASSWORD);
        final String adminAccessToken = AdminAcceptanceSteps.로그인됨(AdminFixture.EMAIL, AdminFixture.PASSWORD);
        final long 메인카테고리 = CategoryAcceptanceSteps.메인카테고리생성됨(CategoryFixture.NAME, CategoryFixture.ORDER, adminAccessToken);
        final long 서브카테고리 = CategoryAcceptanceSteps.서브카테고리생성됨(CategoryFixture.SUB_NAME, CategoryFixture.SUB_ORDER, 메인카테고리, adminAccessToken);

        SellerAcceptanceSteps.회원가입됨(SellerFixture.EMAIL, SellerFixture.NAME, SellerFixture.PASSWORD, SellerFixture.BIRTH, SellerFixture.ADDRESS, SellerFixture.PHONE);
        final String sellerAccessToken = SellerAcceptanceSteps.로그인됨(SellerFixture.EMAIL, SellerFixture.PASSWORD);
        final long 상점 = ShopAcceptanceSteps.상점생성됨(sellerAccessToken, ShopFixture.NAME);

        final ExtractableResponse<Response> responseExtractableResponse = ProductAcceptanceSteps.registerProduct(상점, 서브카테고리, INVALID_NAME, AMOUNT, IMAGE_URL, sellerAccessToken);
        ProductAcceptanceSteps.validateInvalidNameLength(responseExtractableResponse);
    }
}
