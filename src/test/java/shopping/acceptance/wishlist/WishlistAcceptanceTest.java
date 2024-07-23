package shopping.acceptance.wishlist;

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
import shopping.acceptance.wishlist.steps.WishListAcceptanceSteps;
import shopping.utils.fixture.*;

@DisplayName("위시 리스트 인수 테스트")
public class WishlistAcceptanceTest extends AcceptanceTest {

    @DisplayName("상품이 상점에 등록되어 있으면 사용자는 위시 리스트를 등록할 수 있다")
    @Test
    void registerWishList() {
        AdminAcceptanceSteps.회원가입됨(AdminFixture.EMAIL, AdminFixture.NAME, AdminFixture.PASSWORD);
        final String adminAccessToken = AdminAcceptanceSteps.로그인됨(AdminFixture.EMAIL, AdminFixture.PASSWORD);
        final long 메인카테고리 = CategoryAcceptanceSteps.메인카테고리생성됨(CategoryFixture.NAME, CategoryFixture.ORDER, adminAccessToken);
        final long 서브카테고리 = CategoryAcceptanceSteps.서브카테고리생성됨(CategoryFixture.SUB_NAME, CategoryFixture.SUB_ORDER, 메인카테고리, adminAccessToken);

        SellerAcceptanceSteps.회원가입됨(SellerFixture.EMAIL, SellerFixture.NAME, SellerFixture.PASSWORD, SellerFixture.BIRTH, SellerFixture.ADDRESS, SellerFixture.PHONE);
        final String sellerAccessToken = SellerAcceptanceSteps.로그인됨(SellerFixture.EMAIL, SellerFixture.PASSWORD);
        final long 상점 = ShopAcceptanceSteps.상점생성됨(sellerAccessToken, ShopFixture.NAME);

        final long 상품 = ProductAcceptanceSteps.상품등록됨(상점, 서브카테고리, ProductFixture.NAME, ProductFixture.AMOUNT, ProductFixture.IMAGE_URL, sellerAccessToken);
        CustomerAcceptanceSteps.회원가입됨(CustomerFixture.EMAIL, CustomerFixture.NAME, CustomerFixture.PASSWORD, CustomerFixture.BIRTH, CustomerFixture.ADDRESS, CustomerFixture.PHONE);
        final String customerAccessToken = CustomerAcceptanceSteps.로그인됨(CustomerFixture.EMAIL, CustomerFixture.PASSWORD);

        final ExtractableResponse<Response> response = WishListAcceptanceSteps.registerWishlist(상품, customerAccessToken);
        WishListAcceptanceSteps.validate(response);
    }

    @DisplayName("위시 리스트에 등록된 상품이 추가 등록되어도 위시리스트의 상품은 하나이다")
    @Test
    void registerWishListDuplicated() {
        AdminAcceptanceSteps.회원가입됨(AdminFixture.EMAIL, AdminFixture.NAME, AdminFixture.PASSWORD);
        final String adminAccessToken = AdminAcceptanceSteps.로그인됨(AdminFixture.EMAIL, AdminFixture.PASSWORD);
        final long 메인카테고리 = CategoryAcceptanceSteps.메인카테고리생성됨(CategoryFixture.NAME, CategoryFixture.ORDER, adminAccessToken);
        final long 서브카테고리 = CategoryAcceptanceSteps.서브카테고리생성됨(CategoryFixture.SUB_NAME, CategoryFixture.SUB_ORDER, 메인카테고리, adminAccessToken);

        SellerAcceptanceSteps.회원가입됨(SellerFixture.EMAIL, SellerFixture.NAME, SellerFixture.PASSWORD, SellerFixture.BIRTH, SellerFixture.ADDRESS, SellerFixture.PHONE);
        final String sellerAccessToken = SellerAcceptanceSteps.로그인됨(SellerFixture.EMAIL, SellerFixture.PASSWORD);
        final long 상점 = ShopAcceptanceSteps.상점생성됨(sellerAccessToken, ShopFixture.NAME);

        final long 상품 = ProductAcceptanceSteps.상품등록됨(상점, 서브카테고리, ProductFixture.NAME, ProductFixture.AMOUNT, ProductFixture.IMAGE_URL, sellerAccessToken);
        CustomerAcceptanceSteps.회원가입됨(CustomerFixture.EMAIL, CustomerFixture.NAME, CustomerFixture.PASSWORD, CustomerFixture.BIRTH, CustomerFixture.ADDRESS, CustomerFixture.PHONE);
        final String customerAccessToken = CustomerAcceptanceSteps.로그인됨(CustomerFixture.EMAIL, CustomerFixture.PASSWORD);

        WishListAcceptanceSteps.registerWishlist(상품, customerAccessToken);
        final ExtractableResponse<Response> response = WishListAcceptanceSteps.registerWishlist(상품, customerAccessToken);
        WishListAcceptanceSteps.validate(response);
    }
}
