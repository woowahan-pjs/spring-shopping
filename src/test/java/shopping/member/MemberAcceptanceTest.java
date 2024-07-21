package shopping.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shopping.AcceptanceTest;

import static shopping.member.MemberAcceptanceStepTest.*;


@DisplayName("회원 관련 기능")
public class MemberAcceptanceTest extends AcceptanceTest {

    private static final String password = "1234";
    private static final String email = "test@test.com";

    @DisplayName("회원을 생성한다.")
    @Test
    void createMember() {
        // when
        ExtractableResponse<Response> response = createMemberRequest(password, email);

        // then
        createdMember(response);
    }


}
