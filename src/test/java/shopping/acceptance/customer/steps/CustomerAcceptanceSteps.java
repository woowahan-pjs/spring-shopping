package shopping.acceptance.customer.steps;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import shopping.customer.api.dto.CustomerSignInRequest;
import shopping.customer.api.dto.CustomerSignUpRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class CustomerAcceptanceSteps {

    private static final String USER_BASE_URL = "/api/customers";
    private static final String SIGN_UP = "/sign-up";
    private static final String SIGN_IN = "/sign-in";

    public static ExtractableResponse<Response> signUp(final String email, final String name, final String password, final String birth, final String address, final String phone) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new CustomerSignUpRequest(email, name, password, birth, address, phone))
                .when()
                .post(USER_BASE_URL + SIGN_UP)
                .then()
                .extract();
    }

    public static ExtractableResponse<Response> signIn(final String email, final String password) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new CustomerSignInRequest(email, password))
                .when()
                .post(USER_BASE_URL + SIGN_IN)
                .then()
                .extract();
    }

    public static void validateCustomerSignUp(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void validateCustomerSignUpInvalidEmail(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void validateCustomerSignUpInvalidPassword(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void validateCustomerSignUpDuplicatedEmail(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void validateCustomerSignIn(final ExtractableResponse<Response> response) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getString("access_token")).isNotBlank()
        );
    }

    public static void validateCustomerSignInInvalidEmail(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
