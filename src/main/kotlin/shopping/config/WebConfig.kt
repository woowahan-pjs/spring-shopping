package shopping.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import shopping.common.api.CurrentMemberArgumentResolver

@Configuration
class WebConfig(
    private val currentMemberArgumentResolver: CurrentMemberArgumentResolver,
) : WebMvcConfigurer {
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(currentMemberArgumentResolver)
    }
}
