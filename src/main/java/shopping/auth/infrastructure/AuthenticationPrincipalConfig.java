package shopping.auth.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import shopping.auth.application.AuthService;
import shopping.auth.ui.AuthenticationPrincipalArgumentResolver;

import java.util.List;

@Configuration
public class AuthenticationPrincipalConfig implements WebMvcConfigurer {
    private final AuthService authService;

    public AuthenticationPrincipalConfig(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(createAuthenticationPrincipalArgumentResolver());
    }

    @Bean
    public AuthenticationPrincipalArgumentResolver createAuthenticationPrincipalArgumentResolver() {
        return new AuthenticationPrincipalArgumentResolver(authService);
    }
}
