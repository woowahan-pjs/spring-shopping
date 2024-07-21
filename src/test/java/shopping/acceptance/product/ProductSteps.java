package shopping.acceptance.product;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import org.springframework.http.MediaType;

public class ProductSteps {
    private ProductSteps() {
    }

    public static ExtractableResponse<Response> 상품_등록(String name, String imageUrl, Integer price) {
        return RestAssured.given().log().all()
                .body(상품_정보_Map_생성(name, imageUrl, price))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/products")
                .then().log().all()
                .extract();
    }

    public static Map<String, Object> 상품_정보_Map_생성(String name, String imageUrl, Integer price) {
        return Map.of(
                "name", name,
                "imageUrl", imageUrl,
                "price", price
        );
    }

    public static ExtractableResponse<Response> 상품_단건_조회(long id) {
        return RestAssured.given().log().all()
                .when().get("/products/{id}", id)
                .then().log().all()
                .extract();
    }

    public static String 상품_단건_이름_추출(ExtractableResponse<Response> response) {
        return response.jsonPath().getString("name");
    }

    public static String 상품_단건_이미지_URL_추출(ExtractableResponse<Response> response) {
        return response.jsonPath().getString("imageUrl");
    }

    public static int 상품_단건_가격_추출(ExtractableResponse<Response> response) {
        return response.jsonPath().getInt("price");
    }

    public static ExtractableResponse<Response> 상품_목록_조회(int page, int size) {
        return RestAssured.given().log().all()
                .queryParam("offset", page)
                .queryParam("size", size)
                .when().get("/products")
                .then().log().all()
                .extract();
    }


    public static List<String> 상품_목록_이름_추출(ExtractableResponse<Response> response) {
        return response.jsonPath().getList("products.name", String.class);
    }

    public static List<String> 상품_목록_이미지_URL_추출(ExtractableResponse<Response> response) {
        return response.jsonPath().getList("products.imageUrl", String.class);
    }

    public static List<Integer> 상품_목록_가격_추출(ExtractableResponse<Response> response) {
        return response.jsonPath().getList("products.price", Integer.class);
    }

    public static ExtractableResponse<Response> 상품_수정(long id, String name, String imageUrl, int price) {
        return RestAssured.given().log().all()
                .body(상품_정보_Map_생성(name, imageUrl, price))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/products/{id}", id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 상품_삭제(long id) {
        return RestAssured.given().log().all()
                .when().delete("/products/{id}", id)
                .then().log().all()
                .extract();
    }
}
