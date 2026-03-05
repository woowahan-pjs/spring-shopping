package shopping.auth.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import shopping.auth.web.annotation.CurrentMemberId;
import shopping.common.ApiException;
import shopping.common.ErrorCode;

class MemberIdResolverTest {
    private MemberIdResolver memberIdResolver;

    @BeforeEach
    void setUp() {
        memberIdResolver = new MemberIdResolver();
    }

    @Test
    void supportAnnotatedLongParameter() throws Exception {
        MethodParameter parameter = new MethodParameter(TestController.class.getMethod("member", Long.class), 0);

        boolean supported = memberIdResolver.supportsParameter(parameter);

        assertThat(supported).isTrue();
    }

    @Test
    void supportAnnotatedPrimitiveLongParameter() throws Exception {
        MethodParameter parameter = new MethodParameter(TestController.class.getMethod("primitive", long.class), 0);

        boolean supported = memberIdResolver.supportsParameter(parameter);

        assertThat(supported).isTrue();
    }

    @Test
    void rejectNonAnnotatedParameter() throws Exception {
        MethodParameter parameter = new MethodParameter(TestController.class.getMethod("plain", Long.class), 0);

        boolean supported = memberIdResolver.supportsParameter(parameter);

        assertThat(supported).isFalse();
    }

    @Test
    void resolveMemberIdFromRequestAttribute() throws Exception {
        MethodParameter parameter = new MethodParameter(TestController.class.getMethod("member", Long.class), 0);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute(AuthAttributes.MEMBER_ID_ATTRIBUTE, 12L);

        Object result = memberIdResolver.resolveArgument(
                parameter,
                null,
                new ServletWebRequest(request),
                null
        );

        assertThat(result).isEqualTo(12L);
    }

    @Test
    void throwApiExceptionWhenMemberIdMissing() throws Exception {
        MethodParameter parameter = new MethodParameter(TestController.class.getMethod("member", Long.class), 0);
        MockHttpServletRequest request = new MockHttpServletRequest();

        assertThatThrownBy(() -> memberIdResolver.resolveArgument(
                parameter,
                null,
                new ServletWebRequest(request),
                null
        ))
                .isInstanceOf(ApiException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.AUTHENTICATION_TOKEN_INVALID);
    }

    static class TestController {
        public void member(@CurrentMemberId Long memberId) {
        }

        public void primitive(@CurrentMemberId long memberId) {
        }

        public void plain(Long memberId) {
        }
    }
}
