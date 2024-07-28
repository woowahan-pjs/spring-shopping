package shopping.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import shopping.AcceptanceTest;
import shopping.member.dto.MemberResponse;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static shopping.member.MemberAcceptanceStepTest.*;


@DisplayName("회원 관련 기능")
public class MemberAcceptanceTest extends AcceptanceTest {

    private static final String password1 = "1234";
    private static final String email1 = "yalmung@email.com";
    private static final String name1 = "얄뭉";

    private static final String password2 = "1111";
    private static final String email2 = "yalmung@test.com";
    private static final String nickName = "하하";


    @DisplayName("회원을 생성한다.")
    @Test
    void createMember() {
        // when
        ExtractableResponse<Response> response = createMemberRequest(password1, email1, name1, null);

        // then
        createdMember(response);
    }

    @DisplayName("회원 목록을 조회한다.")
    @Test
    void getMembers() {
        // given
        ExtractableResponse<Response> createResponse1 = alreadyCreatedMember(password1, email1, name1, null);
        ExtractableResponse<Response> createResponse2 = alreadyCreatedMember(password2, email2, null, nickName);

        // when
        ExtractableResponse<Response> response = findMemberListRequest();

        // then
        findMemberListResponse(response);
        containMemberList(response, Arrays.asList(createResponse1, createResponse2));
    }



    public static ExtractableResponse<Response> alreadyCreatedMember(String password, String email, String name, String nickName) {
        return createMemberRequest(password, email, name, nickName);
    }

    public static void findMemberListResponse(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }


    public static void containMemberList(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> expectedMbrSns = createdResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());

        List<MemberResponse.MemberDetail> resultMembers = response.jsonPath().getList("members", MemberResponse.MemberDetail.class);
        List<Long> resultMbrSns = resultMembers.stream().map(MemberResponse.MemberDetail::getMbrSn).toList();

        assertThat(resultMbrSns).containsAll(expectedMbrSns);
    }


}
