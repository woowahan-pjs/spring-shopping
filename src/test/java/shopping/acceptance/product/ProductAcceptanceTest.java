package shopping.acceptance.product;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shopping.acceptance.AcceptanceTest;
import shopping.acceptance.admin.steps.AdminAcceptanceSteps;
import shopping.acceptance.category.steps.CategoryAcceptanceSteps;
import shopping.acceptance.customer.steps.CustomerAcceptanceSteps;
import shopping.acceptance.product.steps.ProductAcceptanceSteps;
import shopping.acceptance.seller.steps.SellerAcceptanceSteps;
import shopping.acceptance.shop.steps.ShopAcceptanceSteps;
import shopping.utils.fixture.*;

import static shopping.utils.fixture.ProductFixture.*;

@DisplayName("상품 인수 테스트")
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

        final ExtractableResponse<Response> responseExtractableResponse = ProductAcceptanceSteps.registerProduct(상점, 서브카테고리, NAME, AMOUNT, THUMBNAIL_IMAGE_URL, sellerAccessToken);
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

        final ExtractableResponse<Response> responseExtractableResponse = ProductAcceptanceSteps.registerProduct(상점, 서브카테고리, INVALID_NAME, AMOUNT, THUMBNAIL_IMAGE_URL, sellerAccessToken);
        ProductAcceptanceSteps.validateInvalidNameLength(responseExtractableResponse);
    }

    @DisplayName("상품 이름이 영문 욕설이 들었다면 상품을 등록할 수 없다")
    @Test
    void registerProductInvalidProductNameContainsProfanity() {
        AdminAcceptanceSteps.회원가입됨(AdminFixture.EMAIL, AdminFixture.NAME, AdminFixture.PASSWORD);
        final String adminAccessToken = AdminAcceptanceSteps.로그인됨(AdminFixture.EMAIL, AdminFixture.PASSWORD);
        final long 메인카테고리 = CategoryAcceptanceSteps.메인카테고리생성됨(CategoryFixture.NAME, CategoryFixture.ORDER, adminAccessToken);
        final long 서브카테고리 = CategoryAcceptanceSteps.서브카테고리생성됨(CategoryFixture.SUB_NAME, CategoryFixture.SUB_ORDER, 메인카테고리, adminAccessToken);

        SellerAcceptanceSteps.회원가입됨(SellerFixture.EMAIL, SellerFixture.NAME, SellerFixture.PASSWORD, SellerFixture.BIRTH, SellerFixture.ADDRESS, SellerFixture.PHONE);
        final String sellerAccessToken = SellerAcceptanceSteps.로그인됨(SellerFixture.EMAIL, SellerFixture.PASSWORD);
        final long 상점 = ShopAcceptanceSteps.상점생성됨(sellerAccessToken, ShopFixture.NAME);

        final ExtractableResponse<Response> responseExtractableResponse = ProductAcceptanceSteps.registerProduct(상점, 서브카테고리, PROFANITY_NAME, AMOUNT, THUMBNAIL_IMAGE_URL, sellerAccessToken);
        ProductAcceptanceSteps.validateInvalidNameContainsProfanity(responseExtractableResponse);
    }

    @DisplayName("상품 이름이 허용하지 않는 특수문자가 들어왔을 경우 상품을 등록할 수 없다")
    @Test
    void registerProductInvalidProductNameRegex() {
        AdminAcceptanceSteps.회원가입됨(AdminFixture.EMAIL, AdminFixture.NAME, AdminFixture.PASSWORD);
        final String adminAccessToken = AdminAcceptanceSteps.로그인됨(AdminFixture.EMAIL, AdminFixture.PASSWORD);
        final long 메인카테고리 = CategoryAcceptanceSteps.메인카테고리생성됨(CategoryFixture.NAME, CategoryFixture.ORDER, adminAccessToken);
        final long 서브카테고리 = CategoryAcceptanceSteps.서브카테고리생성됨(CategoryFixture.SUB_NAME, CategoryFixture.SUB_ORDER, 메인카테고리, adminAccessToken);

        SellerAcceptanceSteps.회원가입됨(SellerFixture.EMAIL, SellerFixture.NAME, SellerFixture.PASSWORD, SellerFixture.BIRTH, SellerFixture.ADDRESS, SellerFixture.PHONE);
        final String sellerAccessToken = SellerAcceptanceSteps.로그인됨(SellerFixture.EMAIL, SellerFixture.PASSWORD);
        final long 상점 = ShopAcceptanceSteps.상점생성됨(sellerAccessToken, ShopFixture.NAME);

        final ExtractableResponse<Response> responseExtractableResponse = ProductAcceptanceSteps.registerProduct(상점, 서브카테고리, INVALID_EXPRESS_NAME, AMOUNT, THUMBNAIL_IMAGE_URL, sellerAccessToken);
        ProductAcceptanceSteps.validateInvalidNameContainsProfanity(responseExtractableResponse);
    }

    @DisplayName("모든 사용자는 상점에 등록된 상품을 볼 수 있다")
    @Test
    void readProduct() {
        AdminAcceptanceSteps.회원가입됨(AdminFixture.EMAIL, AdminFixture.NAME, AdminFixture.PASSWORD);
        final String adminAccessToken = AdminAcceptanceSteps.로그인됨(AdminFixture.EMAIL, AdminFixture.PASSWORD);
        final long 메인카테고리 = CategoryAcceptanceSteps.메인카테고리생성됨(CategoryFixture.NAME, CategoryFixture.ORDER, adminAccessToken);
        final long 서브카테고리 = CategoryAcceptanceSteps.서브카테고리생성됨(CategoryFixture.SUB_NAME, CategoryFixture.SUB_ORDER, 메인카테고리, adminAccessToken);

        SellerAcceptanceSteps.회원가입됨(SellerFixture.EMAIL, SellerFixture.NAME, SellerFixture.PASSWORD, SellerFixture.BIRTH, SellerFixture.ADDRESS, SellerFixture.PHONE);
        final String sellerAccessToken = SellerAcceptanceSteps.로그인됨(SellerFixture.EMAIL, SellerFixture.PASSWORD);
        final long 상점 = ShopAcceptanceSteps.상점생성됨(sellerAccessToken, ShopFixture.NAME);

        final long 상품 = ProductAcceptanceSteps.상품등록됨(상점, 서브카테고리, NAME, AMOUNT, THUMBNAIL_IMAGE_URL, sellerAccessToken);
        CustomerAcceptanceSteps.회원가입됨(CustomerFixture.EMAIL, CustomerFixture.NAME, CustomerFixture.PASSWORD, CustomerFixture.BIRTH, CustomerFixture.ADDRESS, CustomerFixture.PHONE);
        final String customerAccessToken = CustomerAcceptanceSteps.로그인됨(CustomerFixture.EMAIL, CustomerFixture.PASSWORD);

        final ExtractableResponse<Response> response = ProductAcceptanceSteps.readById(상품, customerAccessToken);
        ProductAcceptanceSteps.validateRead(response);
    }

    @DisplayName("모든 사용자는 카테고리별 상품 목록을 볼 수 있다")
    @Test
    void readProductInCategory() {
        AdminAcceptanceSteps.회원가입됨(AdminFixture.EMAIL, AdminFixture.NAME, AdminFixture.PASSWORD);
        final String adminAccessToken = AdminAcceptanceSteps.로그인됨(AdminFixture.EMAIL, AdminFixture.PASSWORD);
        final long 메인카테고리 = CategoryAcceptanceSteps.메인카테고리생성됨(CategoryFixture.NAME, CategoryFixture.ORDER, adminAccessToken);
        final long 서브카테고리 = CategoryAcceptanceSteps.서브카테고리생성됨(CategoryFixture.SUB_NAME, CategoryFixture.SUB_ORDER, 메인카테고리, adminAccessToken);

        SellerAcceptanceSteps.회원가입됨(SellerFixture.EMAIL, SellerFixture.NAME, SellerFixture.PASSWORD, SellerFixture.BIRTH, SellerFixture.ADDRESS, SellerFixture.PHONE);
        final String sellerAccessToken = SellerAcceptanceSteps.로그인됨(SellerFixture.EMAIL, SellerFixture.PASSWORD);
        final long 상점 = ShopAcceptanceSteps.상점생성됨(sellerAccessToken, ShopFixture.NAME);
        ProductAcceptanceSteps.상품등록됨(상점, 서브카테고리, NAME, AMOUNT, THUMBNAIL_IMAGE_URL, sellerAccessToken);

        CustomerAcceptanceSteps.회원가입됨(CustomerFixture.EMAIL, CustomerFixture.NAME, CustomerFixture.PASSWORD, CustomerFixture.BIRTH, CustomerFixture.ADDRESS, CustomerFixture.PHONE);
        final String customerAccessToken = CustomerAcceptanceSteps.로그인됨(CustomerFixture.EMAIL, CustomerFixture.PASSWORD);

        final ExtractableResponse<Response> response = ProductAcceptanceSteps.readByCategory(서브카테고리, customerAccessToken);
        ProductAcceptanceSteps.validateReadCategory(response);
    }

    @DisplayName("모든 사용자는 상점의 상품 목록을 볼 수 있다")
    @Test
    void readProductInShop() {
        AdminAcceptanceSteps.회원가입됨(AdminFixture.EMAIL, AdminFixture.NAME, AdminFixture.PASSWORD);
        final String adminAccessToken = AdminAcceptanceSteps.로그인됨(AdminFixture.EMAIL, AdminFixture.PASSWORD);
        final long 메인카테고리 = CategoryAcceptanceSteps.메인카테고리생성됨(CategoryFixture.NAME, CategoryFixture.ORDER, adminAccessToken);
        final long 서브카테고리 = CategoryAcceptanceSteps.서브카테고리생성됨(CategoryFixture.SUB_NAME, CategoryFixture.SUB_ORDER, 메인카테고리, adminAccessToken);

        SellerAcceptanceSteps.회원가입됨(SellerFixture.EMAIL, SellerFixture.NAME, SellerFixture.PASSWORD, SellerFixture.BIRTH, SellerFixture.ADDRESS, SellerFixture.PHONE);
        final String sellerAccessToken = SellerAcceptanceSteps.로그인됨(SellerFixture.EMAIL, SellerFixture.PASSWORD);
        final long 상점 = ShopAcceptanceSteps.상점생성됨(sellerAccessToken, ShopFixture.NAME);
        ProductAcceptanceSteps.상품등록됨(상점, 서브카테고리, NAME, AMOUNT, THUMBNAIL_IMAGE_URL, sellerAccessToken);

        CustomerAcceptanceSteps.회원가입됨(CustomerFixture.EMAIL, CustomerFixture.NAME, CustomerFixture.PASSWORD, CustomerFixture.BIRTH, CustomerFixture.ADDRESS, CustomerFixture.PHONE);
        final String customerAccessToken = CustomerAcceptanceSteps.로그인됨(CustomerFixture.EMAIL, CustomerFixture.PASSWORD);

        final ExtractableResponse<Response> response = ProductAcceptanceSteps.readByShop(상점, customerAccessToken);
        ProductAcceptanceSteps.validateReadShop(response);
    }
}
