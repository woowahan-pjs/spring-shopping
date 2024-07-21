package shopping.acceptance.token;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import shopping.acceptance.utils.AcceptanceTest;
import shopping.acceptance.member.MemberSteps;

public class TokenAcceptanceTest extends AcceptanceTest {
    /**
     * given: 회원 가입을 하고
     * when: 토큰 발급을 요청하면
     * then: 토큰이 발급된다.
     */
    @DisplayName("토큰 발급 테스트")
    @Test
    void generateToken() {
        // given
        String email = "email@email.com";
        String password = "password";
        MemberSteps.회원_가입(email, password);

        // when
        ExtractableResponse<Response> response = TokenSteps.토큰_발급(email, password);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(TokenSteps.토큰_추출(response)).isNotBlank();
    }
}
