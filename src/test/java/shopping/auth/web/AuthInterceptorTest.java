package shopping.auth.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import shopping.auth.access.AccessPolicyResolver;
import shopping.auth.access.AccessType;
import shopping.auth.application.AuthService;
import shopping.member.service.MemberService;

@ExtendWith(MockitoExtension.class)
class AuthInterceptorTest {
    @Mock
    private AuthService authService;

    @Mock
    private MemberService memberService;

    @Mock
    private AccessPolicyResolver accessPolicyResolver;

    private AuthInterceptor authInterceptor;
    private HandlerMethod handlerMethod;

    @BeforeEach
    void setUp() throws Exception {
        authInterceptor = new AuthInterceptor(authService, memberService, accessPolicyResolver);
        handlerMethod = new HandlerMethod(new TestController(), TestController.class.getMethod("endpoint"));
    }

    @Test
    void passWhenPublicEndpoint() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        when(accessPolicyResolver.resolve(handlerMethod)).thenReturn(AccessType.PUBLIC);

        boolean result = authInterceptor.preHandle(request, response, handlerMethod);

        assertThat(result).isTrue();
        verify(authService, never()).authenticate(org.mockito.ArgumentMatchers.anyString());
        verify(memberService, never()).requireActiveSeller(org.mockito.ArgumentMatchers.anyLong());
    }

    @Test
    void authenticateWhenMemberEndpoint() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader("Authorization", "Bearer token");
        when(accessPolicyResolver.resolve(handlerMethod)).thenReturn(AccessType.MEMBER);
        when(authService.authenticate("Bearer token")).thenReturn(1L);

        boolean result = authInterceptor.preHandle(request, response, handlerMethod);

        assertThat(result).isTrue();
        assertThat(request.getAttribute(AuthAttributes.MEMBER_ID_ATTRIBUTE)).isEqualTo(1L);
        verify(memberService, never()).requireActiveSeller(org.mockito.ArgumentMatchers.anyLong());
    }

    @Test
    void authenticateAndValidateSellerWhenSellerEndpoint() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader("Authorization", "Bearer seller-token");
        when(accessPolicyResolver.resolve(handlerMethod)).thenReturn(AccessType.SELLER);
        when(authService.authenticate("Bearer seller-token")).thenReturn(7L);

        boolean result = authInterceptor.preHandle(request, response, handlerMethod);

        assertThat(result).isTrue();
        verify(memberService).requireActiveSeller(7L);
    }

    @Test
    void skipAuthenticationWhenMemberIdAlreadyStored() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setAttribute(AuthAttributes.MEMBER_ID_ATTRIBUTE, 9L);
        when(accessPolicyResolver.resolve(handlerMethod)).thenReturn(AccessType.MEMBER);

        boolean result = authInterceptor.preHandle(request, response, handlerMethod);

        assertThat(result).isTrue();
        verify(authService, never()).authenticate(org.mockito.ArgumentMatchers.anyString());
    }

    @Test
    void passWhenHandlerIsNotHandlerMethod() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean result = authInterceptor.preHandle(request, response, new Object());

        assertThat(result).isTrue();
        verify(accessPolicyResolver, never()).resolve(org.mockito.ArgumentMatchers.any());
    }

    static class TestController {
        public void endpoint() {
        }
    }
}
