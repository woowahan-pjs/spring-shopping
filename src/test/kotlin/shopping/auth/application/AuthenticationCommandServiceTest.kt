package shopping.auth.application

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.equality.shouldBeEqualUsingFields
import io.kotest.matchers.equality.shouldNotBeEqualUsingFields
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import shopping.auth.fixture.TokenFixture
import shopping.global.exception.ApplicationException
import shopping.member.application.MemberQueryRepository
import shopping.member.domain.Member
import shopping.member.fixture.MemberFixture

@DisplayName("AuthenticationCommandService 테스트")
class AuthenticationCommandServiceTest : BehaviorSpec({

    val tokenCommandRepository: TokenCommandRepository = mockk()
    val tokenQueryRepository: TokenQueryRepository = mockk()
    val memberQueryRepository: MemberQueryRepository = mockk()
    val jwtService: JwtService = mockk()
    val authenticationManager: AuthenticationManager = mockk()

    val authenticationCommandService =
        AuthenticationCommandService(
            tokenCommandRepository,
            tokenQueryRepository,
            memberQueryRepository,
            jwtService,
            authenticationManager,
        )

    Given("로그인 요청 시") {
        val loginCommand = MemberFixture.`고객 1`.`로그인 COMMAND 생성`()

        val authentication: Authentication = mockk()
        val member: Member = mockk()
        val authenticationCredentials = TokenFixture.`토큰 1`.`토큰 엔티티 생성`()

        When("가입 되어 있는 회원의 정보로 요청이 왔다면") {
            every { authenticationManager.authenticate(any<UsernamePasswordAuthenticationToken>()) } returns authentication
            every { authentication.principal } returns member
            every { jwtService.generateAuthenticationCredentials(member) } returns authenticationCredentials
            every { tokenCommandRepository.save(authenticationCredentials) } returns authenticationCredentials

            val actual = authenticationCommandService.logIn(loginCommand)

            Then("로그인 후 토큰 쌍을 반환 한다") {
                actual.shouldBeEqualUsingFields(authenticationCredentials)
            }
        }

        When("가입 되어 있지 않은 회원의 정보로 요청이 왔다면") {
            every { authenticationManager.authenticate(any()) } throws BadCredentialsException("Bad credentials")

            Then("예외를 던진다") {
                shouldThrow<BadCredentialsException> {
                    authenticationCommandService.logIn(loginCommand)
                } shouldHaveMessage "Bad credentials"
            }
        }
    }

    Given("refreshToken 을 재발급 할때") {
        val tokenFixture = TokenFixture.`토큰 1`
        val tokenRefreshCommand = tokenFixture.`토큰 재발급 COMMAND 생성`()
        val memberFixture = MemberFixture.`고객 1`
        val email = memberFixture.email
        val member = memberFixture.`회원 엔티티 생성`()
        val jti = "jti"
        val originAuthenticationCredentials = tokenFixture.`토큰 엔티티 생성`()
        val newAuthenticationCredentials = TokenFixture.`토큰 2`.`토큰 엔티티 생성`()

        When("전달 받은 refreshToken 상태가 정상적이라면") {
            every { jwtService.getUsername(originAuthenticationCredentials.refreshToken) } returns email!!
            every { jwtService.getJti(originAuthenticationCredentials.refreshToken) } returns jti
            every { memberQueryRepository.findByEmailAndNotDeleted(email) } returns member
            every { tokenQueryRepository.findByJti(jti) } returns originAuthenticationCredentials
            every { jwtService.isTokenValid(originAuthenticationCredentials.refreshToken, member) } returns true
            every { jwtService.checkTokenExpiredByTokenString(originAuthenticationCredentials.accessToken) } returns true
            every { jwtService.generateAuthenticationCredentials(member) } returns newAuthenticationCredentials
            every { tokenCommandRepository.save(newAuthenticationCredentials) } returns newAuthenticationCredentials
            justRun { tokenCommandRepository.deleteByJti(originAuthenticationCredentials.jti) }

            val actual = authenticationCommandService.refreshToken(tokenRefreshCommand)

            Then("새로운 accessToken 을 반환 한다") {
                actual.shouldNotBeEqualUsingFields(originAuthenticationCredentials)
                    .shouldBeEqualUsingFields(newAuthenticationCredentials)
            }
        }

        When("전달 받은 refreshToken 의 username 으로 찾은 UserDetails 가 없다면") {
            every { jwtService.getUsername(originAuthenticationCredentials.refreshToken) } returns email!!
            every { jwtService.getJti(originAuthenticationCredentials.refreshToken) } returns jti
            every { memberQueryRepository.findByEmailAndNotDeleted(email) } returns null

            Then("예외를 던진다") {
                shouldThrow<ApplicationException> {
                    authenticationCommandService.refreshToken(tokenRefreshCommand)
                } shouldHaveMessage "일치하는 회원 정보를 찾을 수 없습니다."
            }
        }

        When("전달 받은 refreshToken 의 jti 으로 찾은 토큰이 없다면") {
            every { jwtService.getUsername(originAuthenticationCredentials.refreshToken) } returns email!!
            every { jwtService.getJti(originAuthenticationCredentials.refreshToken) } returns jti
            every { memberQueryRepository.findByEmailAndNotDeleted(email) } returns member
            every { tokenQueryRepository.findByJti(jti) } returns null

            Then("예외를 던진다") {
                shouldThrow<ApplicationException> {
                    authenticationCommandService.refreshToken(tokenRefreshCommand)
                } shouldHaveMessage "토큰을 찾을 수 없습니다."
            }
        }

        When("전달 받은 refreshToken 과 저장소에서 찾은 refreshToken 이 다른 경우") {
            every { jwtService.getUsername(originAuthenticationCredentials.refreshToken) } returns email!!
            every { jwtService.getJti(originAuthenticationCredentials.refreshToken) } returns jti
            every { memberQueryRepository.findByEmailAndNotDeleted(email) } returns member
            every { tokenQueryRepository.findByJti(jti) } returns TokenFixture.`토큰 2`.`토큰 엔티티 생성`()

            Then("예외를 던진다") {
                shouldThrow<ApplicationException> {
                    authenticationCommandService.refreshToken(tokenRefreshCommand)
                } shouldHaveMessage "토큰이 일치하지 않습니다."
            }
        }

        When("전달 받은 refreshToken 이 유효하지 않다면") {
            every { jwtService.getUsername(originAuthenticationCredentials.refreshToken) } returns email!!
            every { jwtService.getJti(originAuthenticationCredentials.refreshToken) } returns jti
            every { memberQueryRepository.findByEmailAndNotDeleted(email) } returns member
            every { tokenQueryRepository.findByJti(jti) } returns originAuthenticationCredentials
            every { jwtService.isTokenValid(originAuthenticationCredentials.refreshToken, member) } returns false

            Then("예외를 던진다") {
                shouldThrow<ApplicationException> {
                    authenticationCommandService.refreshToken(tokenRefreshCommand)
                } shouldHaveMessage "리프레시 토큰이 유효하지 않습니다."
            }
        }

        When("전달 받은 토큰의 jti 로 찾은 accessToken 의 만료시간이 지나지 않았다면") {
            every { jwtService.getUsername(originAuthenticationCredentials.refreshToken) } returns email!!
            every { jwtService.getJti(originAuthenticationCredentials.refreshToken) } returns jti
            every { memberQueryRepository.findByEmailAndNotDeleted(email) } returns member
            every { tokenQueryRepository.findByJti(jti) } returns originAuthenticationCredentials
            every { jwtService.isTokenValid(originAuthenticationCredentials.refreshToken, member) } returns true
            every { jwtService.checkTokenExpiredByTokenString(originAuthenticationCredentials.accessToken) } returns false
            justRun { tokenCommandRepository.deleteByJti(jti) }

            Then("예외를 던진다") {
                shouldThrow<ApplicationException> {
                    authenticationCommandService.refreshToken(tokenRefreshCommand)
                } shouldHaveMessage "토큰을 재발급 할 수 없습니다."
            }
        }
    }
})
