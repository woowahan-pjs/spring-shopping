package shopping.acceptance.customer.steps;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import shopping.customer.api.dto.CustomerRegistrationRequest;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerAcceptanceSteps {

    private static final String USER_BASE_URL = "/api/customers";

    public static ExtractableResponse<Response> register(final String email, final String name, final String password, final String birth, final String address, final String phone) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new CustomerRegistrationRequest(email, name, password, birth, address, phone))
                .when()
                .post(USER_BASE_URL)
                .then()
                .extract();
    }

    public static void validateCustomerRegistration(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

}
