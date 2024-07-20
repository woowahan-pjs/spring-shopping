package shopping.member.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import shopping.core.AcceptanceTest;

import java.util.Map;

import static shopping.core.AcceptanceTestUtils.등록_식별자_추출;
import static shopping.core.AcceptanceTestUtils.등록요청_성공;
import static shopping.core.AcceptanceTestUtils.삭제요청_성공;


@DisplayName("회원관련 기능")
@AcceptanceTest
class MemberAcceptanceTest {
    public static final String EMAIL = "test@test.com";
    public static final String PASSWORD = "password";

    @DisplayName("회원가입을 한다.")
    @Test
    void 회원_등록_테스트() {
        final var response = 회원_등록_요청();

        등록요청_성공(response);
    }

    @DisplayName("회원탈퇴를 한다.")
    @Test
    void deleteMember() {
        final var 회원_등록_응답 = 회원_등록_요청();

        final var response = 회원_삭제_요청(등록_식별자_추출(회원_등록_응답));

        삭제요청_성공(response);
    }

    public ExtractableResponse<Response> 회원_등록_요청() {
        final Map<String, ?> body = Map.of("email", EMAIL, "password", PASSWORD);
        return RestAssured
                .given()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/members")
                .then().extract();
    }

    private ExtractableResponse<Response> 회원_삭제_요청(final String id) {
        return RestAssured
                .given().pathParam("id", id)
                .when().delete("/members/{id}")
                .then().extract();
    }

}
