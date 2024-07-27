package shopping.auth.application

import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import jakarta.servlet.FilterChain
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import shopping.auth.domain.AuthenticationCredentials

@DisplayName("JwtAuthenticationFilter 테스트")
class JwtAuthenticationFilterTest : BehaviorSpec({
    val jwtService: JwtService = mockk()
    val userDetailsService: UserDetailsService = mockk()
    val tokenQueryRepository: TokenQueryRepository = mockk()

    val jwtTokenFilter = JwtAuthenticationFilter(jwtService, userDetailsService, tokenQueryRepository)

    beforeEach {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL)
    }

    Given("필터를 요청이 왔을 때") {

        When("필터를 무시 하는 경로일 경우") {
            val request = MockHttpServletRequest()
            val response = MockHttpServletResponse()
            val filterChain: FilterChain = mockk(relaxed = true)
            val securityContext: SecurityContext = mockk()
            request.requestURI = "/api/auth/foo"

            jwtTokenFilter.doFilterInternal(request, response, filterChain)

            Then("SecurityContext 를 업데이트 하지 않고 다음 필터를 바로 호출 한다") {
                verify(exactly = 0) { securityContext.authentication = any<Authentication>() }
                verify(exactly = 1) { filterChain.doFilter(request, response) }
            }
        }

        When("Authorization 헤더가 없는 경우") {
            val request = MockHttpServletRequest()
            val response = MockHttpServletResponse()
            val filterChain: FilterChain = mockk(relaxed = true)
            val securityContext: SecurityContext = mockk()
            request.requestURI = "/api/members"

            jwtTokenFilter.doFilterInternal(request, response, filterChain)

            Then("SecurityContext 를 업데이트 하지 않고 다음 필터를 바로 호출 한다") {
                verify(exactly = 0) { securityContext.authentication = any<Authentication>() }
                verify(exactly = 1) { filterChain.doFilter(request, response) }
            }
        }

        When("Authorization 헤더 값이 'Bearer ' 로 시작하지 않는 경우") {
            val request = MockHttpServletRequest()
            val response = MockHttpServletResponse()
            val filterChain: FilterChain = mockk(relaxed = true)
            val securityContext: SecurityContext = mockk()
            request.requestURI = "/api/members"
            request.addHeader("Authorization", "Bearersdf890asf89asfd")

            jwtTokenFilter.doFilterInternal(request, response, filterChain)

            Then("SecurityContext 를 업데이트 하지 않고 다음 필터를 바로 호출 한다") {
                verify(exactly = 0) { securityContext.authentication = any<Authentication>() }
                verify(exactly = 1) { filterChain.doFilter(request, response) }
            }
        }

        When("헤더의 토큰과 헤더의 토큰 jti 로 토큰 저장소에서 토큰이 일치하지 않는 경우") {
            val request = MockHttpServletRequest()
            val response = MockHttpServletResponse()
            val filterChain: FilterChain = mockk(relaxed = true)
            val securityContext: SecurityContext = mockk()
            val jwt = "sdf890asf89asfd"
            val username = "username"
            val jti = "jti"
            val authenticationCredentials: AuthenticationCredentials = mockk()
            request.requestURI = "/api/members"
            request.addHeader("Authorization", "Bearer $jwt")

            every { jwtService.getUsername(jwt) } returns username
            every { jwtService.getJti(jwt) } returns jti
            every { tokenQueryRepository.findByJti(jti) } returns authenticationCredentials
            every { authenticationCredentials.accessToken } returns "token"

            jwtTokenFilter.doFilterInternal(request, response, filterChain)

            Then("SecurityContext 를 업데이트 하지 않고 다음 필터를 바로 호출 한다") {
                verify(exactly = 0) { securityContext.authentication = any<Authentication>() }
                verify(exactly = 1) { filterChain.doFilter(request, response) }
            }
        }

        When("SecurityContextHolder 의 SecurityContext 에 Authentication 인증 된 객체가 이미 존재하는 경우") {
            val request = MockHttpServletRequest()
            val response = MockHttpServletResponse()
            val filterChain: FilterChain = mockk(relaxed = true)
            val securityContext: SecurityContext = mockk()
            val jwt = "sdf890asf89asfd"
            val username = "username"
            val jti = "jti"
            val authenticationCredentials: AuthenticationCredentials = mockk()
            request.requestURI = "/api/members"
            request.addHeader("Authorization", "Bearer $jwt")

            every { jwtService.getUsername(jwt) } returns username
            every { jwtService.getJti(jwt) } returns jti
            every { tokenQueryRepository.findByJti(jti) } returns authenticationCredentials
            every { authenticationCredentials.accessToken } returns jwt
            mockkStatic(SecurityContextHolder::class)
            every { SecurityContextHolder.getContext() } returns securityContext
            every { securityContext.authentication } returns mockk()

            jwtTokenFilter.doFilterInternal(request, response, filterChain)

            Then("SecurityContext 를 업데이트 하지 않고 다음 필터를 바로 호출 한다") {
                verify(exactly = 0) { securityContext.authentication = any<Authentication>() }
                verify(exactly = 1) { filterChain.doFilter(request, response) }
            }
        }

        When("요청 헤더의 토큰이 유효성 검증을 통과하지 못하면") {
            val request = MockHttpServletRequest()
            val response = MockHttpServletResponse()
            val filterChain: FilterChain = mockk(relaxed = true)
            val securityContext: SecurityContext = mockk()
            val jwt = "sdf890asf89asfd"
            val username = "username"
            val jti = "jti"
            val authenticationCredentials: AuthenticationCredentials = mockk()
            val userDetails: UserDetails = mockk()
            request.requestURI = "/api/members"
            request.addHeader("Authorization", "Bearer $jwt")

            every { jwtService.getUsername(jwt) } returns username
            every { jwtService.getJti(jwt) } returns jti
            every { tokenQueryRepository.findByJti(jti) } returns authenticationCredentials
            every { authenticationCredentials.accessToken } returns jwt
            mockkStatic(SecurityContextHolder::class)
            every { SecurityContextHolder.getContext() } returns securityContext
            every { securityContext.authentication } returns null
            every { userDetailsService.loadUserByUsername(username) } returns userDetails
            every { jwtService.isTokenValid(jwt, userDetails) } returns false

            jwtTokenFilter.doFilterInternal(request, response, filterChain)

            Then("SecurityContext 를 업데이트 하지 않고 다음 필터를 바로 호출 한다") {
                verify(exactly = 0) { securityContext.authentication = any<Authentication>() }
                verify(exactly = 1) { filterChain.doFilter(request, response) }
            }
        }

        When("정상 적인 토큰이 포함 된 요청인 경우") {
            val request = MockHttpServletRequest()
            val response = MockHttpServletResponse()
            val filterChain: FilterChain = mockk(relaxed = true)
            val securityContext: SecurityContext = mockk()
            val jwt = "sdf890asf89asfd"
            val username = "username"
            val jti = "jti"
            val authenticationCredentials: AuthenticationCredentials = mockk()
            val userDetails: UserDetails = mockk()
            request.requestURI = "/api/members"
            request.addHeader("Authorization", "Bearer $jwt")

            every { jwtService.getUsername(jwt) } returns username
            every { jwtService.getJti(jwt) } returns jti
            every { tokenQueryRepository.findByJti(jti) } returns authenticationCredentials
            every { authenticationCredentials.accessToken } returns jwt
            mockkStatic(SecurityContextHolder::class)
            every { SecurityContextHolder.getContext() } returns securityContext
            every { securityContext.authentication } returns null
            every { userDetailsService.loadUserByUsername(username) } returns userDetails
            every { jwtService.isTokenValid(jwt, userDetails) } returns true
            every { userDetails.authorities } returns mockk(relaxed = true)
            justRun { securityContext.authentication = any<Authentication>() }

            jwtTokenFilter.doFilterInternal(request, response, filterChain)

            Then("SecurityContext 를 업데이트 후 다음 필터를 호출 한다") {
                verify(exactly = 1) { securityContext.authentication = any<Authentication>() }
                verify(exactly = 1) { filterChain.doFilter(request, response) }
            }
        }
    }
})
