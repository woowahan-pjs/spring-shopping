package shopping.auth.adapter.in.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

class AuthConfigTest {
    @Test
    @DisplayName("인증 인터셉터를 MVC 설정에 등록한다")
    void addInterceptorsRegisterAuthInterceptor() throws Exception {
        // given
        AuthInterceptor authInterceptor = mock(AuthInterceptor.class);
        MemberIdResolver memberIdResolver = new MemberIdResolver();
        AuthConfig authConfig = new AuthConfig(authInterceptor, memberIdResolver);
        InterceptorRegistry registry = new InterceptorRegistry();

        // when
        authConfig.addInterceptors(registry);

        // then
        List<InterceptorRegistration> registrations = registrationsOf(registry);
        assertThat(registrations).hasSize(1);
    }

    @Test
    @DisplayName("현재 회원 id 리졸버를 MVC 설정에 등록한다")
    void addArgumentResolversRegisterMemberIdResolver() {
        // given
        AuthInterceptor authInterceptor = mock(AuthInterceptor.class);
        MemberIdResolver memberIdResolver = new MemberIdResolver();
        AuthConfig authConfig = new AuthConfig(authInterceptor, memberIdResolver);
        List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();

        // when
        authConfig.addArgumentResolvers(resolvers);

        // then
        assertThat(resolvers).containsExactly(memberIdResolver);
    }

    private List<InterceptorRegistration> registrationsOf(InterceptorRegistry registry) throws Exception {
        Field field = InterceptorRegistry.class.getDeclaredField("registrations");
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<InterceptorRegistration> registrations = (List<InterceptorRegistration>) field.get(registry);
        return registrations;
    }
}
