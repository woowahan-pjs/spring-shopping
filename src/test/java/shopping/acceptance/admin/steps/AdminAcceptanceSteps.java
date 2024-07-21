package shopping.acceptance.admin.steps;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import shopping.admin.api.dto.AdminSignInHttpRequest;
import shopping.admin.api.dto.AdminSignUpHttpRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AdminAcceptanceSteps {
    private static final String SELLER_BASE_URL = "/api/admins";
    private static final String SIGN_UP = "/sign-up";
    private static final String SIGN_IN = "/sign-in";

    public static ExtractableResponse<Response> signUp(final String email, final String name, final String password) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new AdminSignUpHttpRequest(email, name, password))
                .when()
                .post(SELLER_BASE_URL + SIGN_UP)
                .then()
                .extract();
    }

    public static ExtractableResponse<Response> signIn(final String email, final String password) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new AdminSignInHttpRequest(email, password))
                .when()
                .post(SELLER_BASE_URL + SIGN_IN)
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

    public static void validateSellerSignIn(final ExtractableResponse<Response> response) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getString("access_token")).isNotBlank()
        );
    }

    public static void validateSellerSignInNotRegisteredEmail(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void validateSellerSignInWrongPassword(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void validateSellerSignInInvalidEmail(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void validateSellerSignInInvalidPassword(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 회원가입됨(final String email, final String name, final String password) {
        final ExtractableResponse<Response> responseExtractableResponse = signUp(email, name, password);
        validateSellerSignUp(responseExtractableResponse);
    }

    public static String 로그인됨(final String email, final String password) {
        final ExtractableResponse<Response> responseExtractableResponse = signIn(email, password);
        validateSellerSignIn(responseExtractableResponse);
        return responseExtractableResponse.body().jsonPath().getString("access_token");
    }
}
