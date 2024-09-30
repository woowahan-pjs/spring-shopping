package shopping.auth.application

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import shopping.auth.fixture.TokenFixture
import shopping.global.exception.ErrorCode
import java.io.PrintWriter

class CustomLogoutHandlerTest : BehaviorSpec({
    val jwtService: JwtService = mockk()
    val tokenQueryRepository: TokenQueryRepository = mockk()
    val tokenCommandRepository: TokenCommandRepository = mockk()
    val objectMapper: ObjectMapper = mockk()

    val logoutHandler = CustomLogoutHandler(jwtService, tokenQueryRepository, tokenCommandRepository, objectMapper)

    beforeEach {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL)
    }

    Given("로그아웃 요청을 받았을 때") {
        val request: HttpServletRequest = mockk()
        val response: HttpServletResponse = mockk()

        val authenticationCredentials = TokenFixture.`토큰 1`.`토큰 엔티티 생성`()

        When("로그인 된 이용자의 토큰으로 확인이 되면") {
            every { request.getHeader("Authorization") } returns "Bearer ${authenticationCredentials.accessToken}"
            every { jwtService.getJti(authenticationCredentials.accessToken) } returns authenticationCredentials.jti
            every { tokenQueryRepository.findByJti(authenticationCredentials.jti) } returns authenticationCredentials
            justRun { tokenCommandRepository.deleteByJti(authenticationCredentials.jti) }
            mockkStatic(SecurityContextHolder::class)
            justRun { SecurityContextHolder.clearContext() }

            logoutHandler.logout(request, response, mockk())

            Then("토큰을 삭제 한다") {
                verify(exactly = 1) { tokenCommandRepository.deleteByJti(authenticationCredentials.jti) }
            }
        }

        When("요청으로 받은 토큰으로 토큰 저장소에서 토큰을 찾지 못하면") {
            val printWriter: PrintWriter = mockk()
            val jsonResponse = """{"meta":{"code":404,"message":"토큰을 찾을 수 없습니다."}"""
            every { request.getHeader("Authorization") } returns "Bearer ${authenticationCredentials.accessToken}"
            every { jwtService.getJti(authenticationCredentials.accessToken) } returns authenticationCredentials.jti
            every { tokenQueryRepository.findByJti(authenticationCredentials.jti) } returns null
            every { objectMapper.writeValueAsString(any()) } returns jsonResponse
            justRun { response.contentType = any() }
            justRun { response.characterEncoding = any() }
            justRun { response.status = any() }
            every { response.writer } returns printWriter
            justRun { printWriter.write(any<String>()) }

            logoutHandler.logout(request, response, mockk())

            Then("토큰을 삭제하지 않고 에러 응답을 반환 한다") {
                verify(exactly = 1) { response.contentType = "application/json" }
                verify(exactly = 1) { response.characterEncoding = "UTF-8" }
                verify(exactly = 1) { response.status = ErrorCode.TOKEN_NOT_FOUND.status.value() }
                verify(exactly = 1) { printWriter.write(jsonResponse) }
            }
        }
    }
})
