package shopping.acceptance.token;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class TokenSteps {
    private TokenSteps() {
    }

    public static ExtractableResponse<Response> 토큰_발급(String email, String password) {
        return RestAssured.given().log().all()
                .body(로그인_정보_JSON_생성(email, password))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all()
                .extract();
    }

    public static String 로그인_정보_JSON_생성(String email, String password) {
        return """
                {
                    "email": "%s",
                    "password": "%s"
                }
                """.formatted(email, password);
    }

    public static String 토큰_추출(ExtractableResponse<Response> response) {
        return response.jsonPath().getString("token");
    }
}
