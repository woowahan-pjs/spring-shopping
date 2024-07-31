package shopping.wishlist.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import shopping.core.AcceptanceTest;
import shopping.core.AcceptanceTestAuthBase;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static shopping.core.AcceptanceTestUtils.등록_식별자_추출;
import static shopping.core.AcceptanceTestUtils.등록요청_성공;
import static shopping.core.AcceptanceTestUtils.삭제요청_성공;
import static shopping.core.AcceptanceTestUtils.인증_부족_요청;
import static shopping.core.AcceptanceTestUtils.존재하지_않는_리소스_요청;

@DisplayName("위시리스트 관련 기능")
@AcceptanceTest
public class WishlistAcceptanceTest extends AcceptanceTestAuthBase {

    private static final String 상품이름 = "옷";
    private static final String 상품_이미지_경로 = "/clothes/path1";
    private static final int 상품_수량 = 100;
    private static final long 상품_가격 = 10_000L;
    public static final String 존재하지_않는_상품_식별자 = "-1";

    /**
     * Given 로그인이 되어있지 않고
     * When 위시리스트 목록 조회를 하면
     * Then 실패한다
     */
    @DisplayName("로그인 전 위시리스트 목록을 조회하면 실패한다")
    @Test
    void 로그인_전_위시리스트_목록_조회_테스트() {
        // when
        final var 목록_조회_응답 = 로그인_전_상품_목록_조회_요청();

        // then
        인증_부족_요청(목록_조회_응답);
    }

    /**
     * Given 로그인이 되어있고
     * Given 상품이 등록되어 있고
     * When 위시리스트에 상품을 추가하면
     * Then 위시리스트 목록 조회 시 추가한 상품을 찾을 수 있다
     */
    @DisplayName("로그인 후 위시리스트에 상품을 추가한다")
    @Test
    void 로그인_후_위시리스트_상품_추가_테스트() {
        // given
        final var 상품_등록_응답 = 상품_등록_요청();
        final var 상품_식별자 = 등록_식별자_추출(상품_등록_응답);

        // when
        final var 위시리스트_추가_응답 = 로그인_후_위시리스트_추가_요청(상품_식별자);

        // then
        등록요청_성공(위시리스트_추가_응답);

        // then
        로그인_후_위시리스트_목록_조회_시_추가한_상품을_찾을_수_있다(상품_식별자);
    }

    /**
     * Given 로그인이 되어있지 않고
     * Given 상품이 등록되어 있고
     * When 위시리스트에 상품을 추가하면
     * Then 추가 요청에 실패한다.
     */
    @DisplayName("로그인 전 위시리스트에 상품을 추가하면 실패한다")
    @Test
    void 로그인_전_위시리스트_상품_추가_테스트() {
        // given
        final var 상품_등록_응답 = 상품_등록_요청();
        final var 상품_식별자 = 등록_식별자_추출(상품_등록_응답);

        // when
        final var 위시리스트_추가_응답 = 로그인_전_위시리스트_추가_요청(상품_식별자);

        // then
        인증_부족_요청(위시리스트_추가_응답);
    }

    /**
     * Given 로그인이 되어있고
     * Given 상품이 등록되어 있지 않고
     * When 위시리스트에 상품을 추가하면
     * Then 추가 요청에 실패한다.
     */
    @DisplayName("로그인 후 위시리스트에 없는 상품을 추가하면 실패한다")
    @Test
    void 로그인_전_위시리스트_없는_상품_추가_테스트() {
        // when
        final var 위시리스트_추가_응답 = 로그인_후_위시리스트_추가_요청(존재하지_않는_상품_식별자);

        // then
        존재하지_않는_리소스_요청(위시리스트_추가_응답);
    }

    /**
     * Given 로그인이 되어있고
     * Given 위시리스트에 상품이 추가되어 있고
     * When 위시리스트에 상품을 삭제하면
     * Then 위시리스트 목록 조회 시 상품이 제거 되어있다
     */
    @DisplayName("로그인 후 위시리스트 상품을 삭제한다")
    @Test
    void 로그인_후_상품_삭제_테스트() {
        // given
        final var 상품_등록_응답 = 상품_등록_요청();
        final var 상품_식별자 = 등록_식별자_추출(상품_등록_응답);
        로그인_후_위시리스트_추가_요청(상품_식별자);

        // when
        final var 위시리스트_삭제_응답 = 로그인_후_상품_삭제_요청(상품_식별자);

        // then
        삭제요청_성공(위시리스트_삭제_응답);
        로그인_후_위시리스트_목록_조회_시_추가한_상품을_찾을_수_없다(상품_식별자);
    }

    /**
     * Given 로그인이 되어있지 않고
     * Given 위시리스트에 상품이 추가되어 있고
     * When 위시리스트에 상품을 삭제하면
     * Then 삭제 요청에 실패한다
     */
    @DisplayName("로그인 전 위시리스트 상품을 삭제하면 실패한다")
    @Test
    void 로그인_전_상품_삭제_테스트() {
        // given
        final var 상품_등록_응답 = 상품_등록_요청();
        final var 상품_식별자 = 등록_식별자_추출(상품_등록_응답);
        로그인_후_위시리스트_추가_요청(상품_식별자);

        // when
        final var 위시리스트_삭제_응답 = 로그인_전_상품_삭제_요청(상품_식별자);

        // then
        인증_부족_요청(위시리스트_삭제_응답);
    }

    private ExtractableResponse<Response> 로그인_후_상품_목록_조회_요청() {
        return RestAssured
                .given()
                .auth().oauth2(accessToken)
                .when().get("/wishlist")
                .then().extract();
    }

    private ExtractableResponse<Response> 로그인_전_상품_목록_조회_요청() {
        return RestAssured
                .given()
                .log().all()
                .when().get("/wishlist")
                .then().extract();
    }

    public ExtractableResponse<Response> 상품_등록_요청() {
        final Map<String, ?> body = Map.of("name", 상품이름, "imagePath", 상품_이미지_경로, "amount", 상품_수량, "price", 상품_가격);
        return RestAssured
                .given()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/products")
                .then().extract();
    }

    public ExtractableResponse<Response> 로그인_전_위시리스트_추가_요청(final String productId) {
        return RestAssured
                .given()
                .body(Map.of("productId", productId))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/wishlist")
                .then().extract();
    }

    public ExtractableResponse<Response> 로그인_후_위시리스트_추가_요청(final String productId) {
        return RestAssured
                .given()
                .auth().oauth2(accessToken)
                .body(Map.of("productId", productId))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/wishlist")
                .then().extract();
    }


    private void 로그인_후_위시리스트_목록_조회_시_추가한_상품을_찾을_수_있다(final String id) {
        final List<String> productIds = 로그인_후_상품_목록_조회_요청()
                .jsonPath()
                .getList("products.id", String.class);

        assertThat(productIds).containsAnyOf(id);
    }

    private ExtractableResponse<Response> 로그인_전_상품_삭제_요청(final String 상품_식별자) {
        return RestAssured
                .given().queryParam("productId", 상품_식별자)
                .when().delete("/wishlist")
                .then().extract();
    }

    private ExtractableResponse<Response> 로그인_후_상품_삭제_요청(final String 상품_식별자) {
        return RestAssured
                .given().queryParam("productId", 상품_식별자)
                .auth().oauth2(accessToken)
                .when().delete("/wishlist")
                .then().extract();
    }

    private void 로그인_후_위시리스트_목록_조회_시_추가한_상품을_찾을_수_없다(final String id) {
        final List<String> productIds = 로그인_후_상품_목록_조회_요청()
                .jsonPath()
                .getList("products.id", String.class);

        assertThat(productIds).doesNotContain(id);
    }
}
