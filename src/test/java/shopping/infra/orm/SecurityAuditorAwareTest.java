package shopping.infra.orm;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import shopping.auth.domain.Role;
import shopping.infra.security.UserPrincipal;

class SecurityAuditorAwareTest {

    private SecurityAuditorAware auditorAware = new SecurityAuditorAware();

    @AfterEach
    void clear() {
        SecurityContextHolder.clearContext();
    }

    @Nested
    @DisplayName("로그인 정보를 기반으로 등록자 정보를 가져올 때,")
    class getCurrentAuditor {

        @Test
        @DisplayName("성공적으로 로그인 회원의 ID를 가져옵니다.")
        void success() {
            // given
            final Long userId = 703L;

            final UserDetails userDetails =
                    UserPrincipal.generate(userId, "test@test.com", Role.CUSTOMER);
            final Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails, "", userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // when
            final Long result = auditorAware.getCurrentAuditor().get();

            // then
            assertThat(result).isEqualTo(userId);
        }

        @Test
        @DisplayName("존재하지 않는 경우 디폴트(0)을 가져옵니다.")
        void isEmpty() {
            // given
            final Long expectedResult = 0L;

            // when
            final Long result = auditorAware.getCurrentAuditor().get();

            // then
            assertThat(result).isEqualTo(expectedResult);
        }
    }
}
