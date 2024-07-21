package shopping.acceptance.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class MemberSteps {
    private MemberSteps() {
    }

    public static ExtractableResponse<Response> 회원_가입(String email, String password) {
        return RestAssured.given().log().all()
                .body(회원_가입_정보_JSON_생성(email, password))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/members")
                .then().log().all()
                .extract();
    }

    public static String 회원_가입_정보_JSON_생성(String email, String password) {
        return """
                {
                    "email": "%s",
                    "password": "%s"
                }
                """.formatted(email, password);
    }
}
