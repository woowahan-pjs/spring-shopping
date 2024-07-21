package shopping.acceptance.category;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shopping.acceptance.AcceptanceTest;
import shopping.acceptance.admin.steps.AdminAcceptanceSteps;
import shopping.acceptance.category.steps.CategoryAcceptanceSteps;
import shopping.acceptance.customer.steps.CustomerAcceptanceSteps;
import shopping.acceptance.seller.steps.SellerAcceptanceSteps;
import shopping.utils.fixture.AdminFixture;
import shopping.utils.fixture.CustomerFixture;
import shopping.utils.fixture.SellerFixture;

import static shopping.utils.fixture.CategoryFixture.*;


@DisplayName("카테고리 인수 테스트")
public class CategoryAcceptanceTest extends AcceptanceTest {

        @Test
        void scenario() {
        }

        @DisplayName("어드민은 메인 카테고리를 생성할 수 있다")
        @Test
        void registerMainCategory() {
            AdminAcceptanceSteps.회원가입됨(AdminFixture.EMAIL, AdminFixture.NAME, AdminFixture.PASSWORD);
            final String accessToken = AdminAcceptanceSteps.로그인됨(AdminFixture.EMAIL, AdminFixture.PASSWORD);
            final ExtractableResponse<Response> response = CategoryAcceptanceSteps.registerMain(NAME, ORDER, accessToken);
            CategoryAcceptanceSteps.validate(response);
        }

        @DisplayName("고객은 메인 카테고리를 생성할 수 없다")
        @Test
        void registerMainCategoryInvalidCustomer() {
            CustomerAcceptanceSteps.회원가입됨(CustomerFixture.EMAIL, CustomerFixture.NAME, CustomerFixture.PASSWORD, CustomerFixture.BIRTH, CustomerFixture.ADDRESS, CustomerFixture.PHONE);
            final String accessToken = CustomerAcceptanceSteps.로그인됨(CustomerFixture.EMAIL, CustomerFixture.PASSWORD);
            final ExtractableResponse<Response> response = CategoryAcceptanceSteps.registerMain(NAME, ORDER, accessToken);
            CategoryAcceptanceSteps.validateInvalidCustomer(response);
        }

        @DisplayName("판매자는 메인 카테고리를 생성할 수 없다")
        @Test
        void registerMainCategoryInvalidSeller() {
            SellerAcceptanceSteps.회원가입됨(SellerFixture.EMAIL, SellerFixture.NAME, SellerFixture.PASSWORD, SellerFixture.BIRTH, SellerFixture.ADDRESS, SellerFixture.PHONE);
            final String accessToken = SellerAcceptanceSteps.로그인됨(SellerFixture.EMAIL, SellerFixture.PASSWORD);
            final ExtractableResponse<Response> response = CategoryAcceptanceSteps.registerMain(NAME, ORDER, accessToken);
            CategoryAcceptanceSteps.validateInvalidSeller(response);
        }

        @DisplayName("이미 등록된 이름이 있다면 동일한 이름의 메인 카테고리를 생성할 수 없다")
        @Test
        void registerMainCategoryInvalidDuplicatedName() {
            AdminAcceptanceSteps.회원가입됨(AdminFixture.EMAIL, AdminFixture.NAME, AdminFixture.PASSWORD);
            final String accessToken = AdminAcceptanceSteps.로그인됨(AdminFixture.EMAIL, AdminFixture.PASSWORD);
            CategoryAcceptanceSteps.registerMain(NAME, ORDER, accessToken);
            final ExtractableResponse<Response> response = CategoryAcceptanceSteps.registerMain(NAME, ORDER, accessToken);
            CategoryAcceptanceSteps.validateDuplicatedName(response);
        }

        @DisplayName("어드민은 서브 카테고리를 추가할 수 있다")
        @Test
        void registerSubCategory() {
            AdminAcceptanceSteps.회원가입됨(AdminFixture.EMAIL, AdminFixture.NAME, AdminFixture.PASSWORD);
            final String accessToken = AdminAcceptanceSteps.로그인됨(AdminFixture.EMAIL, AdminFixture.PASSWORD);
            final long 메인카테고리_아이디 = CategoryAcceptanceSteps.메인카테고리생성됨(NAME, ORDER, accessToken);
            CategoryAcceptanceSteps.registerSub(SUB_NAME, SUB_ORDER, 메인카테고리_아이디, accessToken);
        }

        @DisplayName("카테고리를 숨김 처리할 수 있다")
        @Test
        void hideCategory() {
        }
}
