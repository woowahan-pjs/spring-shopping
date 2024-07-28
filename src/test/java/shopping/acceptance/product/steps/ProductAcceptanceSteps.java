package shopping.acceptance.product.steps;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import shopping.product.infrastructure.api.dto.ProductRegistrationDetailedImageHttpRequest;
import shopping.product.infrastructure.api.dto.ProductRegistrationHttpRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class ProductAcceptanceSteps {
    private static final String BASE_URL = "/internal-api/shops";
    private static final String PRODUCTS = "/products";
    private static final String PRODUCT_BASE_URL = "/api/products";

    public static ExtractableResponse<Response> registerProduct(final long shopId,
                                                                final long subCategoryId,
                                                                final String name,
                                                                final long amount,
                                                                final String thumbnailImageUrl,
                                                                final List<String> detailedImageUrls,
                                                                final String accessToken
                                                                ) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(new Header("Authorization", "Bearer " + accessToken))
                .body(
                        new ProductRegistrationHttpRequest(name, amount, thumbnailImageUrl, subCategoryId,
                        new ProductRegistrationDetailedImageHttpRequest(detailedImageUrls))
                )
                .when()
                .post(BASE_URL + "/" + shopId + PRODUCTS)
                .then()
                .extract();
    }

    public static ExtractableResponse<Response> readById(final long productId, final String accessToken) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(new Header("Authorization", "Bearer " + accessToken))
                .when()
                .get(PRODUCT_BASE_URL + "/" + productId)
                .then()
                .extract();
    }

    public static ExtractableResponse<Response> readByCategory(final long categoryId, final String accessToken) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(new Header("Authorization", "Bearer " + accessToken))
                .when()
                .get(PRODUCT_BASE_URL + "/categories/" + categoryId)
                .then()
                .extract();
    }

    public static ExtractableResponse<Response> readByShop(final long shopsId, final String accessToken) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(new Header("Authorization", "Bearer " + accessToken))
                .when()
                .get(PRODUCT_BASE_URL + "/shops/" + shopsId)
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
                () -> assertThat(response.body().jsonPath().getString("thumbnail_image_url")).isNotNull(),
                () -> assertThat(response.body().jsonPath().getString("shop_name")).isNotNull(),
                () -> assertThat(response.body().jsonPath().getString("seller_name")).isNotNull(),
                () -> assertThat(response.body().jsonPath().getString("category_name")).isNotNull()
        );
    }

    public static void validateReadCategory(final ExtractableResponse<Response> response) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.body().jsonPath().getList("products")).isNotEmpty()
        );
    }

    public static void validateReadShop(final ExtractableResponse<Response> response) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.body().jsonPath().getList("products")).isNotEmpty()
        );
    }

    public static long 상품등록됨(final long 상점, final long 서브카테고리, final String productName, final long amount, final String thumbnailImageUrl, final List<String> detailedImageUrls,  final String sellerAccessToken) {
        final ExtractableResponse<Response> response = ProductAcceptanceSteps.registerProduct(상점, 서브카테고리, productName, amount, thumbnailImageUrl, detailedImageUrls, sellerAccessToken);
        ProductAcceptanceSteps.validate(response);
        return response.body().jsonPath().getLong("id");
    }
}
