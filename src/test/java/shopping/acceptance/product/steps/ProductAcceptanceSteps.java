package shopping.acceptance.product.steps;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import shopping.product.infrastructure.api.dto.ProductRegistrationHttpRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class ProductAcceptanceSteps {
    private static final String BASE_URL = "/internal-api/shops";
    private static final String PRODUCTS = "/products";

    public static ExtractableResponse<Response> registerProduct(final long shopId, final long subCategoryId, final String name, final long amount, final String imageUrl, final String accessToken) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(new Header("Authorization", "Bearer " + accessToken))
                .body(new ProductRegistrationHttpRequest(name, amount, imageUrl, subCategoryId))
                .when()
                .post(BASE_URL + "/" + shopId + PRODUCTS)
                .then()
                .extract();
    }

    public static ExtractableResponse<Response> readById(final long shopId, final long productId, final String accessToken) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(new Header("Authorization", "Bearer " + accessToken))
                .when()
                .get(BASE_URL + "/" + shopId + PRODUCTS + "/" + productId)
                .then()
                .extract();
    }

    public static void validate(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void validateInvalidNameLength(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void validateInvalidNameContainsProfanity(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void validateRead(final ExtractableResponse<Response> response) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.body().jsonPath().getString("product_name")).isNotNull(),
                () -> assertThat(response.body().jsonPath().getLong("amount")).isNotNull(),
                () -> assertThat(response.body().jsonPath().getString("image_url")).isNotNull(),
                () -> assertThat(response.body().jsonPath().getString("shop_name")).isNotNull(),
                () -> assertThat(response.body().jsonPath().getString("seller_name")).isNotNull(),
                () -> assertThat(response.body().jsonPath().getString("category_name")).isNotNull()
        );
    }

    public static long 상품등록됨(final long 상점, final long 서브카테고리, final String productName, final long amount, final String imageUrl, final String sellerAccessToken) {
        final ExtractableResponse<Response> response = ProductAcceptanceSteps.registerProduct(상점, 서브카테고리, productName, amount, imageUrl, sellerAccessToken);
        ProductAcceptanceSteps.validate(response);
        return response.body().jsonPath().getLong("id");
    }
}
