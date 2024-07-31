package shopping.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

public class AuthSteps {

    private AuthSteps() {
    }

    public static ExtractableResponse<Response> 로그인_요청(final String email, final String password) {
        return RestAssured
                .given()
                .body(Map.of("email", email, "password", password))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/auth/login")
                .then().extract();
    }
}
