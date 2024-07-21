package shopping.acceptance.wishproduct;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import shopping.acceptance.member.MemberSteps;
import shopping.acceptance.product.ProductSteps;
import shopping.acceptance.token.TokenSteps;
import shopping.acceptance.utils.AcceptanceTest;

public class WishProductAcceptanceTest extends AcceptanceTest {
    /**
     * given: 가입한 회원이 있고, 등록된 상품이 있을 때
     * when: 회원이 상품을 위시 리스트 등록 요청하면
     * then: 위시 리스트에 상품이 등록된다
     */
    @DisplayName("위시리스트 등록 테스트")
    @Test
    void test1() {
        // given
        ProductSteps.상품_등록("name", "imageUrl", 1000);

        String email = "email@email.com";
        String password = "password";
        MemberSteps.회원_가입(email, password);
        String token = TokenSteps.토큰_추출(TokenSteps.토큰_발급(email, password));

        // when
        ExtractableResponse<Response> response = WishProductSteps.위시_리스트_등록(token, 1L);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * given: 회원이 있고, 위시 리스트에 등록된 상품이 있을 떄
     * when: 회원의 위시 리스트 조회를 요청하면
     * then: 위시 리스트에 포함된 상품 목록이 조회된다.
     */
    @DisplayName("위시리스트 조회 테스트")
    @Test
    void test2() {
        // given
        String name = "name";
        String imageUrl = "imageUrl";
        int price = 1000;
        ProductSteps.상품_등록(name, imageUrl, price);

        String email = "email@email.com";
        String password = "password";
        MemberSteps.회원_가입(email, password);
        String token = TokenSteps.토큰_추출(TokenSteps.토큰_발급(email, password));

        WishProductSteps.위시_리스트_등록(token, 1L);

        // when
        ExtractableResponse<Response> response = WishProductSteps.위시_리스트_조회(token);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(WishProductSteps.위시리스트_상품_이름_추출(response)).containsOnly(name);
        assertThat(WishProductSteps.위시리스트_상품_이미지_URL_추출(response)).containsOnly(imageUrl);
        assertThat(WishProductSteps.위시리스트_상품_가격_추출(response)).containsOnly(price);
    }

    /**
     * given: 회원이 있고, 위시 리스트에 등록된 상품이 있을 떄
     * when: 회원이 위시 리스트 상품 삭제 요청을 하면
     * then: 해당 상품이 삭제된다.
     */
    @DisplayName("위시리스트 삭제 테스트")
    @Test
    void test3() {
        // given
        ProductSteps.상품_등록("name", "imageUrl", 1000);

        String email = "email@email.com";
        String password = "password";
        MemberSteps.회원_가입(email, password);
        String token = TokenSteps.토큰_추출(TokenSteps.토큰_발급(email, password));

        WishProductSteps.위시_리스트_등록(token, 1L);

        // when
        ExtractableResponse<Response> response = WishProductSteps.위시_리스트_삭제(token, 1L);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        ExtractableResponse<Response> responseOfProduct = WishProductSteps.위시_리스트_조회(token);
        assertThat(WishProductSteps.위시리스트_상품_이름_추출(responseOfProduct)).isEmpty();
        assertThat(WishProductSteps.위시리스트_상품_이미지_URL_추출(responseOfProduct)).isEmpty();
        assertThat(WishProductSteps.위시리스트_상품_가격_추출(responseOfProduct)).isEmpty();
    }
}
