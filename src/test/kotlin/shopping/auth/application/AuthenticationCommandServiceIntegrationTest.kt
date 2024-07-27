package shopping.auth.application

import io.kotest.core.annotation.DisplayName
import io.kotest.matchers.nulls.shouldNotBeNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import shopping.auth.application.command.TokenRefreshCommand
import shopping.auth.domain.AuthenticationCredentials
import shopping.member.application.MemberCommandRepository
import shopping.member.fixture.MemberFixture
import shopping.support.KotestIntegrationTestSupport
import java.util.UUID

@DisplayName("AuthenticationCommandService 통합 테스트")
class AuthenticationCommandServiceIntegrationTest : KotestIntegrationTestSupport() {
    @Autowired
    private lateinit var authenticationCommandService: AuthenticationCommandService

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var memberCommandRepository: MemberCommandRepository

    @Autowired
    private lateinit var tokenProvider: TokenProvider

    @Autowired
    private lateinit var jwtProperties: JwtProperties

    @Autowired
    private lateinit var tokenCommandRepository: TokenCommandRepository

    init {
        Given("email 과 loginPassword 를 받아") {
            val loginCommand = MemberFixture.`고객 1`.`로그인 COMMAND 생성`()
            val member = MemberFixture.`고객 1`.`회원 엔티티 생성`(passwordEncoder)
            memberCommandRepository.save(member)

            When("로그인 후") {
                val actual = authenticationCommandService.logIn(loginCommand)

                Then("토큰 쌍을 반환 한다") {
                    actual.shouldNotBeNull()
                }
            }
        }

        Given("refreshToken 을 받아") {
            val member = MemberFixture.`고객 1`.`회원 엔티티 생성`(passwordEncoder)
            memberCommandRepository.save(member)
            val jti = UUID.randomUUID().toString()
            val accessToken = tokenProvider.buildToken(member, jti, 0L)
            val refreshToken = tokenProvider.buildToken(member, jti, jwtProperties.refreshTokenExpiration)
            val authenticationCredentials =
                tokenCommandRepository.save(AuthenticationCredentials(jti, accessToken, refreshToken))
            val tokenRefreshCommand = TokenRefreshCommand(authenticationCredentials.refreshToken)

            When("accessToken 이 만료되었다면") {
                val actual = authenticationCommandService.refreshToken(tokenRefreshCommand)

                Then("새로운 토큰을 발급 한다") {
                    actual.shouldNotBeNull()
                }
            }
        }
    }
}
