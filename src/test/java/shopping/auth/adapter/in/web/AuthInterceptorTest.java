package shopping.auth.adapter.in.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import shopping.auth.adapter.in.web.access.AccessPolicyResolver;
import shopping.auth.adapter.in.web.access.AccessType;
import shopping.auth.service.AuthService;
import shopping.member.service.MemberService;

@ExtendWith(MockitoExtension.class)
@DisplayName("[인증] 인증 인터셉터 단위 테스트")
class AuthInterceptorTest {
    @Mock
    private AuthService authService;

    @Mock
    private MemberService memberService;

    @Mock
    private AccessPolicyResolver accessPolicyResolver;

    private AuthInterceptor authInterceptor;
    private HandlerMethod controllerHandler;

    @BeforeEach
    void setUp() throws Exception {
        authInterceptor = new AuthInterceptor(authService, memberService, accessPolicyResolver);
        controllerHandler = createControllerHandler();
    }

    @Test
    @DisplayName("공개 엔드포인트는 인증을 건너뛴다")
    void skipAuthenticationForPublicEndpoint() throws Exception {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        when(accessPolicyResolver.resolve(controllerHandler)).thenReturn(AccessType.PUBLIC);

        // when
        boolean result = authInterceptor.preHandle(request, response, controllerHandler);

        // then
        assertThat(result).isTrue();
        verify(authService, never()).authenticate(anyString());
        verify(memberService, never()).requireActiveSeller(anyLong());
    }

    @Test
    @DisplayName("회원 엔드포인트는 인증 후 현재 회원 id를 저장한다")
    void authenticateMemberEndpoint() throws Exception {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader("Authorization", "Bearer token");
        when(accessPolicyResolver.resolve(controllerHandler)).thenReturn(AccessType.MEMBER);
        when(authService.authenticate("Bearer token")).thenReturn(1L);

        // when
        boolean result = authInterceptor.preHandle(request, response, controllerHandler);

        // then
        assertThat(result).isTrue();
        assertThat(request.getAttribute(AuthAttributes.MEMBER_ID_ATTRIBUTE)).isEqualTo(1L);
        verify(memberService, never()).requireActiveSeller(anyLong());
    }

    @Test
    @DisplayName("판매자 엔드포인트는 인증 후 판매자 권한까지 확인한다")
    void authenticateAndValidateSellerEndpoint() throws Exception {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader("Authorization", "Bearer seller-token");
        when(accessPolicyResolver.resolve(controllerHandler)).thenReturn(AccessType.SELLER);
        when(authService.authenticate("Bearer seller-token")).thenReturn(7L);

        // when
        boolean result = authInterceptor.preHandle(request, response, controllerHandler);

        // then
        assertThat(result).isTrue();
        verify(memberService).requireActiveSeller(7L);
    }

    @Test
    @DisplayName("이미 인증된 요청이면 저장된 회원 id를 재사용한다")
    void reuseStoredMemberIdWhenRequestAlreadyAuthenticated() throws Exception {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setAttribute(AuthAttributes.MEMBER_ID_ATTRIBUTE, 9L);
        when(accessPolicyResolver.resolve(controllerHandler)).thenReturn(AccessType.MEMBER);

        // when
        boolean result = authInterceptor.preHandle(request, response, controllerHandler);

        // then
        assertThat(result).isTrue();
        verify(authService, never()).authenticate(anyString());
    }

    @Test
    @DisplayName("컨트롤러 메서드가 아니면 인증을 적용하지 않는다")
    void skipAuthenticationWhenHandlerIsNotControllerMethod() throws Exception {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        boolean result = authInterceptor.preHandle(request, response, new Object());

        // then
        assertThat(result).isTrue();
        verify(accessPolicyResolver, never()).resolve(any());
    }

    private HandlerMethod createControllerHandler() throws NoSuchMethodException {
        return new HandlerMethod(new TestEndpointController(), TestEndpointController.class.getMethod("endpoint"));
    }

    static class TestEndpointController {
        public void endpoint() {
        }
    }
}
