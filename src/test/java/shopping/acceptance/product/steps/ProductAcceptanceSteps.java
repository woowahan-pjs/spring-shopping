package shopping.acceptance.product.steps;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import shopping.product.api.dto.ProductRegistrationHttpRequest;

import static org.assertj.core.api.Assertions.assertThat;

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

    public static void validate(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void validateInvalidNameLength(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
