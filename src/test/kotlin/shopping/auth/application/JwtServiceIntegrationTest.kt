package shopping.auth.application

import io.kotest.core.annotation.DisplayName
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeBlank
import org.springframework.beans.factory.annotation.Autowired
import shopping.member.application.MemberCommandRepository
import shopping.member.fixture.MemberFixture
import shopping.support.KotestIntegrationTestSupport

@DisplayName("JwtService 통합 테스트")
class JwtServiceIntegrationTest : KotestIntegrationTestSupport() {
    @Autowired
    private lateinit var jwtService: JwtService

    @Autowired
    private lateinit var memberCommandRepository: MemberCommandRepository

    @Autowired
    private lateinit var tokenProvider: TokenProvider

    init {
        Given("토큰을 전달 받아") {
            val memberEntity = memberCommandRepository.save(MemberFixture.`고객 1`.`회원 엔티티 생성`())
            val jti = "jti"
            val token = tokenProvider.buildToken(memberEntity, jti, 5000L)

            When("username 을 추출 하여") {
                val actual = jwtService.getUsername(token)

                Then("반환 한다") {
                    actual shouldBe "member1@domain.com"
                }
            }

            When("jti 를 추출 하여") {
                val actual = jwtService.getJti(token)
                Then("반환 한다") {
                    actual shouldBe "jti"
                }
            }
        }

        Given("userDetails 와 jti 를 받아") {
            val memberEntity = memberCommandRepository.save(MemberFixture.`고객 1`.`회원 엔티티 생성`())
            val jti = "jti"

            When("새로운 refreshToken 을 발급 후") {
                val actual = jwtService.generateRefreshToken(memberEntity, jti)

                Then("반환 한다") {
                    actual.shouldNotBeBlank()
                }
            }
        }

        Given("userDetails 를 받아") {
            val memberEntity = memberCommandRepository.save(MemberFixture.`고객 1`.`회원 엔티티 생성`())

            When("새로운 토큰 쌍을 발급 후") {
                val actual = jwtService.generateAuthenticationCredentials(memberEntity)

                Then("반환 한다") {
                    actual.shouldNotBeNull()
                }
            }
        }

        Given("토큰과 userDetails 를 받아") {
            val memberEntity = memberCommandRepository.save(MemberFixture.`고객 1`.`회원 엔티티 생성`())
            val jti = "jti"
            val expiration = 5000L
            val token = tokenProvider.buildToken(memberEntity, jti, expiration)

            When("토큰의 유효성을 확인 후") {
                val actual = jwtService.isTokenValid(token, memberEntity)

                Then("결과를 반환 한다") {
                    actual.shouldBeTrue()
                }
            }
        }

        Given("토큰의 claim 중") {
            val memberEntity = memberCommandRepository.save(MemberFixture.`고객 1`.`회원 엔티티 생성`())
            val jti = "jti"
            val expiration = 1L
            val token = tokenProvider.buildToken(memberEntity, jti, expiration)

            When("만료 시간이 지났다면") {
                val actual = jwtService.checkTokenExpiredByTokenString(token)

                Then("true 를 반환 한다") {
                    actual.shouldBeTrue()
                }
            }
        }
    }
}
