package shopping.auth.application

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter
import shopping.auth.domain.AuthenticationCredentials

class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsService,
    private val tokenQueryRepository: TokenQueryRepository,
) : OncePerRequestFilter() {
    public override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        if (isByPassUrl(request)) {
            filterChain.doFilter(request, response)
            return
        }

        val authHeader: String? = request.getHeader("Authorization")
        if (isInvalidBearerHeader(authHeader)) {
            filterChain.doFilter(request, response)
            return
        }

        setSecurityContext(authHeader, request)

        filterChain.doFilter(request, response)
    }

    private fun isInvalidBearerHeader(authHeader: String?) = authHeader == null || !authHeader.startsWith(BEARER_HEADER_PREFIX)

    private fun isByPassUrl(request: HttpServletRequest) = request.servletPath.contains(AUTH_URL)

    private fun setSecurityContext(
        authHeader: String?,
        request: HttpServletRequest,
    ) {
        val jwt = authHeader!!.substring(7)
        val username = jwtService.getUsername(jwt)
        val authenticationCredentials = tokenQueryRepository.findByJti(jwtService.getJti(jwt))

        if (isImpossibleSetSecurityContext(authenticationCredentials, jwt)) {
            return
        }

        val userDetails = userDetailsService.loadUserByUsername(username)

        if (jwtService.isTokenValid(jwt, userDetails)) {
            updateContext(userDetails, request)
        }
    }

    private fun isImpossibleSetSecurityContext(
        authenticationCredentials: AuthenticationCredentials?,
        jwt: String,
    ): Boolean = authenticationCredentials?.accessToken != jwt || SecurityContextHolder.getContext().authentication != null

    private fun updateContext(
        userDetails: UserDetails,
        request: HttpServletRequest,
    ) = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
        .also {
            it.details = WebAuthenticationDetailsSource().buildDetails(request)
            SecurityContextHolder.getContext().authentication = it
        }

    companion object {
        private const val AUTH_URL = "/api/auth"
        private const val BEARER_HEADER_PREFIX = "Bearer "
    }
}
