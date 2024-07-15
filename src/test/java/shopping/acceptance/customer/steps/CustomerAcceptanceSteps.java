package shopping.acceptance.customer.steps;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerAcceptanceSteps {

    private static final String USER_BASE_URL = "/api/customers";

    public static ExtractableResponse<Response> createCustomer() {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(customerRegistrationRequest())
                .when()
                .post(USER_BASE_URL)
                .then()
                .extract();
    }

    private static CustomerRegistrationRequest customerRegistrationRequest() {
        return new CustomerRegistrationRequest();
    }

    public static void validateCreateCustomer(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
}

class CustomerRegistrationRequest {
    private String email;
    private String name;
    private String password;
    private String brith;
    private String address;
    private String phone;

    public CustomerRegistrationRequest() {
    }

    public CustomerRegistrationRequest(final String email, final String name, final String password, final String brith, final String address, final String phone) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.brith = brith;
        this.address = address;
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getBrith() {
        return brith;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }
}