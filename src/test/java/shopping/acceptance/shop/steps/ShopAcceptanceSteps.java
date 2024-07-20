package shopping.acceptance.shop.steps;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class ShopAcceptanceSteps {
    private static final String SHOP_BASE_URL = "/api/sellers/shops";
    private static final String REGISTRATION = "/registration";

    public static ExtractableResponse<Response> registerShop(final String accessToken, final String name) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(new Header("Authorization", "Bearer " + accessToken))
                .body(new ShopRegistrationRequest(name))
                .when()
                .post(SHOP_BASE_URL + REGISTRATION)
                .then()
                .extract();
    }

    public static void validateRegistration(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
}

class ShopRegistrationRequest {
    private String name;

    public ShopRegistrationRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}