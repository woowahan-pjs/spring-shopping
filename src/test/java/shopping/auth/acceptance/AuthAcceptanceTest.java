package shopping.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import shopping.core.AcceptanceTest;
import shopping.core.AcceptanceTestAuthBase;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@AcceptanceTest
public class AuthAcceptanceTest extends AcceptanceTestAuthBase {

    /**
     * When email 과 password 으로 로그인 요청시 유효한 회원이면
     * Then 토큰을 발급 받을 수 있다
     */
    @DisplayName("유효한 Email 과 Password 로 토큰을 발급 받을 수 있다.")
    @Test
    void 로그인_성공_테스트() {
        final ExtractableResponse<Response> response = 로그인_요청(EMAIL, PASSWORD);

        토큰_발급_성공(response);
    }

    /**
     * When email 과 password 으로 로그인 요청시 유효하지 않은 회원이면
     * Then 에러가 난다
     */
    @DisplayName("유효하지 않은 Email 과 Password 로 로그인시 실패한다.")
    @Test
    void 로그인_실패_테스트() {
        final ExtractableResponse<Response> response = 로그인_요청(EMAIL, "WRONG_PASSWORD");

        토큰_발급_실패(response);
    }

    public static ExtractableResponse<Response> 로그인_요청(final String email, final String password) {
        return RestAssured
                .given()
                .body(Map.of("email", email, "password", password))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/auth/login")
                .then().extract();
    }


    private static void 토큰_발급_성공(final ExtractableResponse<Response> response) {
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    private void 토큰_발급_실패(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

}
