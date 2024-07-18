package shopping.acceptance.seller.steps;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import shopping.seller.api.dto.SellerSignUpHttpRequest;

import static org.assertj.core.api.Assertions.assertThat;

public class SellerAcceptanceTest {
    private static final String SELLER_BASE_URL = "/api/sellers";
    private static final String SIGN_UP = "/sign-up";
    private static final String SIGN_IN = "/sign-in";

    public static ExtractableResponse<Response> signUp(final String email, final String name, final String password, final String birth, final String address, final String phone) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new SellerSignUpHttpRequest(email, name, password, birth, address, phone))
                .when()
                .post(SELLER_BASE_URL + SIGN_UP)
                .then()
                .extract();
    }

    public static void validateSellerSignUp(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void validateCustomerSignUpInvalidEmail(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void validateCustomerSignUpInvalidPassword(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
