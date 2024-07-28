package shopping.acceptance.customer.steps;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import shopping.customer.infrastructure.api.dto.CustomerSignInHttpRequest;
import shopping.customer.infrastructure.api.dto.CustomerSignUpHttpRequest;

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
                .body(new CustomerSignUpHttpRequest(email, name, password, birth, address, phone))
                .when()
                .post(USER_BASE_URL + SIGN_UP)
                .then()
                .extract();
    }

    public static ExtractableResponse<Response> signIn(final String email, final String password) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new CustomerSignInHttpRequest(email, password))
                .when()
                .post(USER_BASE_URL + SIGN_IN)
                .then()
                .extract();
    }

    public static ExtractableResponse<Response> getCustomerInfo(final String accessToken) {
        return RestAssured
                .given()
                .header(new Header("Authorization", "Bearer " + accessToken))
                .when()
                .get(USER_BASE_URL)
                .then()
                .extract();
    }

    public static ExtractableResponse<Response> signOut(final String accessToken) {
        return RestAssured
                .given()
                .header(new Header("Authorization", "Bearer " + accessToken))
                .when()
                .post(USER_BASE_URL + "/sign-out")
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
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void validateCustomerSignIn(final ExtractableResponse<Response> response) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getString("access_token")).isNotBlank()
        );
    }

    public static void validateCustomerSignInNotRegisteredEmail(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void validateCustomerSignInWrongPassword(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void validateCustomerSignInInvalidEmail(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void validateCustomerSignInInvalidPassword(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void validateCustomerInfo(final ExtractableResponse<Response> response, final String name) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.body().jsonPath().getString("name")).isEqualTo(name)
        );
    }

    public static void validateSignOut(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 회원가입됨(final String email, final String name, final String password, final String birth, final String address, final String phone) {
        final ExtractableResponse<Response> response = signUp(email, name, password, birth, address, phone);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static String 로그인됨(final String email, final String password) {
        final ExtractableResponse<Response> responseExtractableResponse = signIn(email, password);
        validateCustomerSignIn(responseExtractableResponse);
        return responseExtractableResponse.body().jsonPath().getString("access_token");
    }
}
