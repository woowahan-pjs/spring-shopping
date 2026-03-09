package shopping.e2e;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import shopping.common.ErrorResponse;
import shopping.e2e.support.AbstractE2eTest;
import shopping.e2e.support.AuthSession;

class AuthMemberE2eTest extends AbstractE2eTest {
    @Nested
    class Register {
        @Test
        @DisplayName("회원 가입은 access token과 refresh cookie를 발급한다")
        void registerIssueTokens() {
            AuthSession session = registerMember(uniqueEmail("user"), "password123!");

            assertThat(session.accessToken()).isNotBlank();
            assertThat(session.refreshCookie()).startsWith("refreshToken=");
        }
    }

    @Nested
    class Login {
        @Test
        @DisplayName("로그인은 새 access token과 refresh cookie를 발급한다")
        void loginIssueTokens() {
            String email = uniqueEmail("user");
            registerMember(email, "password123!");

            AuthSession session = login(email, "password123!");

            assertThat(session.accessToken()).isNotBlank();
            assertThat(session.refreshCookie()).startsWith("refreshToken=");
        }
    }

    @Nested
    class Refresh {
        @Test
        @DisplayName("refresh는 access token과 refresh cookie를 회전한다")
        void refreshRotateTokens() {
            AuthSession session = registerMember(uniqueEmail("user"), "password123!");

            AuthSession refreshedSession = refresh(session);

            assertThat(refreshedSession.accessToken()).isNotBlank();
            assertThat(refreshedSession.refreshCookie()).isNotEqualTo(session.refreshCookie());
        }

        @Test
        @DisplayName("refresh cookie가 없으면 401을 반환한다")
        void refreshRejectWhenCookieIsMissing() {
            ResponseEntity<String> response = post("/api/auth/refresh", null, jsonHeaders());

            assertStatus(response, HttpStatus.UNAUTHORIZED);
            ErrorResponse errorResponse = readError(response);
            assertThat(errorResponse.code()).isEqualTo("REFRESH_TOKEN_REQUIRED");
        }

        @Test
        @DisplayName("refresh cookie가 잘못되면 401을 반환한다")
        void refreshRejectWhenCookieIsInvalid() {
            HttpHeaders headers = jsonHeaders();
            headers.add(HttpHeaders.COOKIE, "refreshToken=invalid-token");

            ResponseEntity<String> response = post("/api/auth/refresh", null, headers);

            assertStatus(response, HttpStatus.UNAUTHORIZED);
            ErrorResponse errorResponse = readError(response);
            assertThat(errorResponse.code()).isEqualTo("REFRESH_TOKEN_INVALID");
        }
    }
}
