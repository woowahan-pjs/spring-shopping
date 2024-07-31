package shopping.core;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import shopping.auth.acceptance.AuthAcceptanceTest;
import shopping.member.domain.Member;
import shopping.member.repository.MemberRepository;

import static shopping.auth.acceptance.AuthSteps.로그인_요청;

@AcceptanceTest
public abstract class AcceptanceTestAuthBase {

    public static final String EMAIL = "authAcceptance@email.com";
    public static final String PASSWORD = "password";

    protected String accessToken;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void acceptanceTestAuthBaseSetUp() {
        memberRepository.save(new Member(EMAIL, PASSWORD));
        final ExtractableResponse<Response> response = 로그인_요청(EMAIL, PASSWORD);
        accessToken = response.jsonPath().getString("accessToken");
    }
}
