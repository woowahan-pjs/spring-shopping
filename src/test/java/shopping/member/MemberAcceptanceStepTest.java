package shopping.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import shopping.member.dto.MemberRequest;

import static org.assertj.core.api.Assertions.assertThat;


public class MemberAcceptanceStepTest {
    public static ExtractableResponse<Response> createMemberRequest(String password, String email) {
        MemberRequest.RegMember regMemberRequest = MemberRequest.RegMember.of(password, email);

        return RestAssured
                .given().log().all()
                .body(regMemberRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/members")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> findMemberListRequest() {
        return RestAssured
                .given().log().all()
                .when().get("/members")
                .then().log().all()
                .extract();
    }



    public static void createdMember(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }


}
