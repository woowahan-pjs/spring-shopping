package shopping.auth;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import shopping.token.application.TokenService;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private final TokenService tokenService;

    public AuthConfig(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver(tokenService));
    }
}
