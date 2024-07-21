package shopping.global.config

import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher
import org.springframework.web.servlet.handler.HandlerMappingIntrospector
import shopping.auth.application.JwtAuthenticationFilter
import shopping.auth.application.JwtService
import shopping.auth.application.TokenQueryRepository
import shopping.auth.application.JwtProperties
import shopping.member.application.MemberQueryRepository

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(JwtProperties::class)
class SecurityConfig(
    private val memberQueryRepository: MemberQueryRepository,
    private val jwtService: JwtService,
    private val logoutHandler: LogoutHandler,
    private val entryPoint: AuthenticationEntryPoint,
    private val accessDeniedHandler: AccessDeniedHandler
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationProvider(): AuthenticationProvider =
        DaoAuthenticationProvider().apply {
            setUserDetailsService(userDetailsService())
            setPasswordEncoder(passwordEncoder())
        }

    @Bean
    fun userDetailsService(): UserDetailsService = UserDetailsService {
        memberQueryRepository.findByEmailAndNotDeleted(it) ?: throw UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다.")
    }

    @Bean
    fun jwtAuthenticationFilter(userDetailsService: UserDetailsService, tokenRepository: TokenQueryRepository): JwtAuthenticationFilter =
        JwtAuthenticationFilter(jwtService, userDetailsService, tokenRepository)

    @Bean
    fun mvcRequestMatcherBuilder(introspector: HandlerMappingIntrospector): MvcRequestMatcher.Builder = MvcRequestMatcher.Builder(introspector)

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager = config.authenticationManager

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        mvc: MvcRequestMatcher.Builder,
        authenticationProvider: AuthenticationProvider,
        jwtAuthenticationFilter: JwtAuthenticationFilter
    ): SecurityFilterChain =
        http.run {
            csrf { it.disable() }
            headers { headers ->
                headers.frameOptions { it.disable() }
            }
            authorizeHttpRequests {
                it
                    .requestMatchers(PathRequest.toH2Console()).permitAll()
                    .requestMatchers(*createMvcRequestMatcherForWhitelist(mvc)).permitAll()
                    .requestMatchers(createMvcRequestMatcher("/api/members", mvc, HttpMethod.POST)).permitAll()
                    .anyRequest()
                    .authenticated()
            }
            sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            authenticationProvider(authenticationProvider)
            addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            logout {
                it.logoutUrl("/api/auth/logout")
                    .addLogoutHandler(logoutHandler)
                    .logoutSuccessHandler { req, res, auth -> SecurityContextHolder.clearContext() }
            }
            exceptionHandling {
                it.authenticationEntryPoint(entryPoint)
                    .accessDeniedHandler(accessDeniedHandler)
            }
            build()
        }

    private fun createMvcRequestMatcher(url: String, mvc: MvcRequestMatcher.Builder, method: HttpMethod? = null): MvcRequestMatcher =
        mvc.pattern(url).apply { method?.let { setMethod(it) } }

    private fun createMvcRequestMatcherForWhitelist(mvc: MvcRequestMatcher.Builder): Array<MvcRequestMatcher> =
        WHITE_LIST_URLS.map { createMvcRequestMatcher(it, mvc) }.toTypedArray()

    companion object {
        private val WHITE_LIST_URLS = arrayOf("/api/auth/**", "/error")
    }

}
