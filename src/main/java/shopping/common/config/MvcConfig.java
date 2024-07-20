package shopping.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import shopping.common.auth.AuthorizationArgumentResolver;

import java.util.List;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    private final AuthorizationArgumentResolver authorizationArgumentResolver;

    public MvcConfig(final AuthorizationArgumentResolver authorizationArgumentResolver) {
        this.authorizationArgumentResolver = authorizationArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authorizationArgumentResolver);
    }
}
