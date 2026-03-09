package shopping.auth.adapter.in.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import shopping.auth.adapter.in.web.annotation.CurrentMemberId;
import shopping.common.ApiException;
import shopping.common.ErrorCode;

class MemberIdResolverTest {
    private MemberIdResolver memberIdResolver;

    @BeforeEach
    void setUp() {
        memberIdResolver = new MemberIdResolver();
    }

    @Test
    @DisplayName("CurrentMemberId가 붙은 Long 파라미터를 지원한다")
    void supportAnnotatedLongParameter() throws Exception {
        MethodParameter parameter = new MethodParameter(TestController.class.getMethod("member", Long.class), 0);

        boolean supported = memberIdResolver.supportsParameter(parameter);

        assertThat(supported).isTrue();
    }

    @Test
    @DisplayName("CurrentMemberId가 붙은 primitive long 파라미터를 지원한다")
    void supportAnnotatedPrimitiveLongParameter() throws Exception {
        MethodParameter parameter = new MethodParameter(TestController.class.getMethod("primitive", long.class), 0);

        boolean supported = memberIdResolver.supportsParameter(parameter);

        assertThat(supported).isTrue();
    }

    @Test
    @DisplayName("애너테이션이 없으면 지원하지 않는다")
    void rejectNonAnnotatedParameter() throws Exception {
        MethodParameter parameter = new MethodParameter(TestController.class.getMethod("plain", Long.class), 0);

        boolean supported = memberIdResolver.supportsParameter(parameter);

        assertThat(supported).isFalse();
    }

    @Test
    @DisplayName("CurrentMemberId가 있어도 Long 타입이 아니면 지원하지 않는다")
    void rejectAnnotatedNonLongParameter() throws Exception {
        MethodParameter parameter = new MethodParameter(TestController.class.getMethod("text", String.class), 0);

        boolean supported = memberIdResolver.supportsParameter(parameter);

        assertThat(supported).isFalse();
    }

    @Test
    @DisplayName("요청 속성에서 현재 회원 id를 꺼낸다")
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
    @DisplayName("현재 회원 id가 없으면 인증 예외를 던진다")
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

        public void text(@CurrentMemberId String memberId) {
        }
    }
}
