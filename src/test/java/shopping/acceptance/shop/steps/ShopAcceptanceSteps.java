package shopping.acceptance.shop.steps;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import shopping.shop.api.dto.ShopRegistrationRequest;

import static org.assertj.core.api.Assertions.assertThat;

public class ShopAcceptanceSteps {
    private static final String SHOP_BASE_URL = "/api/sellers/shops";

    public static ExtractableResponse<Response> registerShop(final String accessToken, final String name) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(new Header("Authorization", "Bearer " + accessToken))
                .body(new ShopRegistrationRequest(name))
                .when()
                .post(SHOP_BASE_URL)
                .then()
                .extract();
    }

    public static void validateRegistration(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void validateRegistrationInvalidCustomer(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
