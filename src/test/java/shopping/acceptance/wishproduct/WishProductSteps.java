package shopping.acceptance.wishproduct;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class WishProductSteps {
    private WishProductSteps() {
    }

    public static ExtractableResponse<Response> 위시_리스트_등록(String token, long productId) {
        Map<String, Long> productIdMap = 상품_ID_MAP_생성(productId);

        return RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, createTokenValueFormat(token))
                .body(productIdMap)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/wish-products")
                .then().log().all()
                .extract();
    }

    private static String createTokenValueFormat(String token) {
        return "Bearer %s".formatted(token);
    }

    private static Map<String, Long> 상품_ID_MAP_생성(long productId) {
        return Map.of("productId", productId);
    }

    public static ExtractableResponse<Response> 위시_리스트_조회(String token) {
        return RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, createTokenValueFormat(token))
                .when().get("/wish-products")
                .then().log().all()
                .extract();
    }

    public static List<String> 위시리스트_상품_이름_추출(ExtractableResponse<Response> response) {
        return response.jsonPath().getList("wishProducts.name", String.class);
    }

    public static List<String> 위시리스트_상품_이미지_URL_추출(ExtractableResponse<Response> response) {
        return response.jsonPath().getList("wishProducts.imageUrl", String.class);
    }

    public static List<Integer> 위시리스트_상품_가격_추출(ExtractableResponse<Response> response) {
        return response.jsonPath().getList("wishProducts.price", Integer.class);
    }

    public static ExtractableResponse<Response> 위시_리스트_삭제(String token, long productId) {
        Map<String, Long> productIdMap = 상품_ID_MAP_생성(productId);

        return RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, createTokenValueFormat(token))
                .body(productIdMap)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/wish-products")
                .then().log().all()
                .extract();
    }
}
