package shopping.infra.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import jakarta.servlet.ServletException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;

import shopping.auth.domain.Role;

class JwtAuthenticationFilterTest {

    private static final String AUTHORIZATION_BEARER = "Bearer ";

    private final String secretKey = "this-is-a-longer-test-secret-key-123456";
    private final Long expiration = 600000L;

    private final JwtTokenProvider provider =
            new JwtTokenProvider(new JwtTokenProperties(secretKey, expiration));
    private final AuthenticationEntryPoint authenticationEntryPoint =
            new CustomAuthenticationEntryPoint(new ObjectMapper());

    private final JwtAuthenticationFilter authenticationFilter =
            new JwtAuthenticationFilter(provider, authenticationEntryPoint);

    @Nested
    @DisplayName("토큰을 검증할 때,")
    class valid {

        @Nested
        @DisplayName("헤더에 `Authorization`이")
        class authorization {

            @Test
            @DisplayName("존재하지 않으면 401를 반환합니다.")
            void isEmptyAuthorization() throws ServletException, IOException {
                // given
                MockHttpServletRequest request = new MockHttpServletRequest();
                MockHttpServletResponse response = new MockHttpServletResponse();
                MockFilterChain filterChain = new MockFilterChain();

                // when
                authenticationFilter.doFilterInternal(request, response, filterChain);

                // then
                assertSoftly(
                        it -> {
                            it.assertThat(response.getStatus())
                                    .isEqualTo(HttpStatus.UNAUTHORIZED.value());
                            it.assertThat(getResponseMessage(response)).contains("인증 정보가 없습니다.");
                        });
            }

            @ParameterizedTest
            @NullAndEmptySource
            @ValueSource(strings = {"INVALID "})
            @DisplayName("`Bearer`로 시작하지 않으면 401를 반환합니다.")
            void invalidPrefix(final String invalidPrefix) throws ServletException, IOException {
                // given
                final String token =
                        invalidPrefix
                                + provider.generateToken(
                                        UserPrincipal.generate(1L, "test@test.com", Role.CUSTOMER));

                MockHttpServletRequest request = new MockHttpServletRequest();
                request.addHeader(HttpHeaders.AUTHORIZATION, token);

                MockHttpServletResponse response = new MockHttpServletResponse();
                MockFilterChain filterChain = new MockFilterChain();

                // when
                authenticationFilter.doFilterInternal(request, response, filterChain);

                // then
                assertSoftly(
                        it -> {
                            it.assertThat(response.getStatus())
                                    .isEqualTo(HttpStatus.UNAUTHORIZED.value());
                            it.assertThat(getResponseMessage(response)).contains("잘못된 요청입니다.");
                        });
            }
        }

        @Test
        @DisplayName("유효한 토큰이 들어왔을 때, 인증에 성공합니다.")
        void success() throws ServletException, IOException {
            // given
            final String token =
                    AUTHORIZATION_BEARER
                            + provider.generateToken(
                                    UserPrincipal.generate(1L, "test@test.com", Role.CUSTOMER));

            MockHttpServletRequest request = new MockHttpServletRequest();
            request.addHeader(HttpHeaders.AUTHORIZATION, token);

            MockHttpServletResponse response = new MockHttpServletResponse();
            MockFilterChain filterChain = new MockFilterChain();

            // when
            authenticationFilter.doFilterInternal(request, response, filterChain);

            // then
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        }
    }

    @Nested
    @DisplayName("특정 URI의 경우 filter를 생략하고자 할 때,")
    class shouldNotFilter {

        @ParameterizedTest
        @ValueSource(strings = {"/swagger-ui", "/h2-console", "/v3/api-docs", "/favicon.ico"})
        @DisplayName("생략 여부가 true로 반환됩니다.")
        void isTrue(final String requestUri) {
            // given
            MockHttpServletRequest request =
                    new MockHttpServletRequest(HttpMethod.GET.name(), requestUri);

            // when
            final boolean result = authenticationFilter.shouldNotFilter(request);

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("해당하지 않는 URI의 경우 false를 반환합니다.")
        void isFalse() {
            // given
            final String requestUri = "/test";
            MockHttpServletRequest request =
                    new MockHttpServletRequest(HttpMethod.GET.name(), requestUri);

            // when
            final boolean result = authenticationFilter.shouldNotFilter(request);

            // then
            assertThat(result).isFalse();
        }
    }

    private static String getResponseMessage(MockHttpServletResponse response) {
        try {
            return response.getContentAsString();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
