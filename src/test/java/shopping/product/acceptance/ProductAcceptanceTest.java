package shopping.product.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import shopping.core.AcceptanceTest;
import shopping.product.application.dto.ProductResponse;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayName("상품 관련 기능")
@AcceptanceTest
public class ProductAcceptanceTest {

    private static final Long 상품_식별자 = 1L;
    private static final String 상품이름 = "옷";
    private static final String 수정된_상품이름 = "수정옷";
    private static final String 상품_이미지_경로 = "/clothes/path1";
    private static final int 상품_수량 = 100;
    private static final long 상품_가격 = 10_000L;

    /**
     * When 상품을 등록하면
     * Then 상품이 등록된다
     * Then 상품 목록 조회 시 등록한 상품을 찾을 수 있다
     */
    @DisplayName("상품을 등록한다")
    @Test
    void 상품_등록_테스트() {
        // when
        final ExtractableResponse<Response> response = 상품_등록_요청();

        // then
        상품_등록_요청이_성공한다(response);

        // then
        상품_목록_조회_시_상품을_찾을_수_있다(상품이름);
    }

    /**
     * Given 상품을 등록하고
     * When 상품을 상세 조회하면
     * Then 상품의 정보를 조회할 수 있다
     */
    @DisplayName("상품 상세를 조회한다")
    @Test
    void 상품_상세_조회_테스트() {
        // given
        final ExtractableResponse<Response> 상품_등록_응답 = 상품_등록_요청();

        // when
        final ExtractableResponse<Response> response = 상품_상세_조회_요청(상품_식별자(상품_등록_응답));

        // then
        상품이_상세_조회_된다(response);
    }

    /**
     * Given 상품을 등록하고
     * When 상품을 수정하면
     * Then 상품 목록 조회 시 수정된 상품을 찾을 수 있다
     */
    @DisplayName("상품 수정한다")
    @Test
    void 상품_수정_테스트() {
        // given
        final ExtractableResponse<Response> 상품_등록_응답 = 상품_등록_요청();

        // when
        final ExtractableResponse<Response> response = 상품_수정_요청(상품_식별자(상품_등록_응답));

        // then
        상품_목록_조회_시_상품을_찾을_수_있다(수정된_상품이름);
    }

    /**
     * Given 상품을 등록하고
     * When 상품을 삭제하면
     * Then 상품 목록 조회 시 상품이 제거 되어있다
     */
    @DisplayName("상품 삭제한다")
    @Test
    void 상품_삭제_테스트() {
        // given
        final ExtractableResponse<Response> 상품_등록_응답 = 상품_등록_요청();

        // when
        final ExtractableResponse<Response> response = 상품_삭제_요청(상품_식별자(상품_등록_응답));

        // then
        상품_목록_조회_시_상품을_찾을_수_없다(상품이름);
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

    private static void 상품_등록_요청이_성공한다(final ExtractableResponse<Response> response) {
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            final ProductResponse productResponse = response.as(ProductResponse.class);
            softly.assertThat(productResponse.getName()).isEqualTo(상품이름);
            softly.assertThat(productResponse.getImagePath()).isEqualTo(상품_이미지_경로);
            softly.assertThat(productResponse.getAmount()).isEqualTo(상품_수량);
            softly.assertThat(productResponse.getPrice()).isEqualTo(상품_가격);
        });
    }

    private void 상품_목록_조회_시_상품을_찾을_수_있다(final String name) {
        final List<String> lineNames = RestAssured
                .given()
                .when().get("/products")
                .then().extract()
                .jsonPath()
                .getList("name", String.class);

        assertThat(lineNames).containsAnyOf(name);
    }

    private static String 상품_식별자(final ExtractableResponse<Response> 상품_등록_응답) {
        final String[] split = 상품_등록_응답.header("Location").split("/");
        return split[split.length - 1];
    }

    private ExtractableResponse<Response> 상품_상세_조회_요청(final String id) {
        return RestAssured
                .given().pathParam("id", id)
                .when().get("/products/{id}")
                .then().extract();
    }

    private void 상품이_상세_조회_된다(final ExtractableResponse<Response> response) {
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            final ProductResponse productResponse = response.as(ProductResponse.class);
            softly.assertThat(productResponse.getId()).isEqualTo(상품_식별자);
        });
    }

    private ExtractableResponse<Response> 상품_수정_요청(final String 상품_식별자) {
        final Map<String, ?> body = Map.of("name", 수정된_상품이름, "imagePath", 상품_이미지_경로, "amount", 상품_수량, "price", 상품_가격);
        return RestAssured
                .given().pathParam("id", 상품_식별자)
                .body(body)
                .when().put("/products")
                .then().extract();
    }

    private ExtractableResponse<Response> 상품_삭제_요청(final String 상품_식별자) {
        return RestAssured
                .given().pathParam("id", 상품_식별자)
                .when().delete("/products")
                .then().extract();
    }

    private void 상품_목록_조회_시_상품을_찾을_수_없다(final String name) {
        final List<String> lineNames = RestAssured
                .given()
                .when().get("/products")
                .then().extract()
                .jsonPath()
                .getList("name", String.class);

        assertThat(lineNames).doesNotContain(name);
    }
}
