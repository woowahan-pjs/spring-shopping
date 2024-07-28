package shopping.auth;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import shopping.auth.dto.TokenRequest;
import shopping.wishlist.dto.WishRequest;


public class AuthAcceptanceStepTest {
    public static ExtractableResponse<Response> login(String email, String password) {
        TokenRequest tokenRequest = TokenRequest.of(email, password);

        return RestAssured
                .given().log().all()
                .body(tokenRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }
}
