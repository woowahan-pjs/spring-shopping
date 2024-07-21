package shopping.product;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import shopping.member.dto.MemberRequest;
import shopping.product.dto.ProductRequest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;


public class ProductAcceptanceStepTest {
    public static ExtractableResponse<Response> createProductRequest(String name, BigDecimal price) {
        ProductRequest.RegProduct regProductRequest = ProductRequest.RegProduct.of(name, price);

        return RestAssured
                .given().log().all()
                .body(regProductRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/products")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> createProductRequest(String name, BigDecimal price, String image) {
        ProductRequest.RegProduct regProductRequest = ProductRequest.RegProduct.of(name, price, image);

        return RestAssured
                .given().log().all()
                .body(regProductRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/products")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> findProductListRequest() {
        return RestAssured
                .given().log().all()
                .when().get("/products")
                .then().log().all()
                .extract();
    }



    public static void createdProduct(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }


}
