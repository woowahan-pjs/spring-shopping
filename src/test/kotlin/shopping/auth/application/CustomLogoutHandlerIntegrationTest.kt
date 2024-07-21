package shopping.auth.application

import io.kotest.core.annotation.DisplayName
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import shopping.member.application.MemberCommandRepository
import shopping.member.fixture.MemberFixture
import shopping.support.KotestIntegrationTestSupport

@DisplayName("CustomLogoutHandler 통합 테스트")
class CustomLogoutHandlerIntegrationTest : KotestIntegrationTestSupport() {
    @Autowired
    private lateinit var memberCommandRepository: MemberCommandRepository

    @Autowired
    private lateinit var jwtService: JwtService

    @Autowired
    private lateinit var tokenCommandRepository: TokenCommandRepository

    @Autowired
    private lateinit var tokenQueryRepository: TokenQueryRepository

    init {
        Given("로그아웃 요청을 받으면") {
            val member = memberCommandRepository.save(MemberFixture.`고객 1`.`회원 엔티티 생성`())
            val authenticationCredentials = jwtService.generateAuthenticationCredentials(member)
            val jti = authenticationCredentials.jti
            tokenCommandRepository.save(authenticationCredentials)

            val request =
                HttpHeaders().apply { set("Authorization", "Bearer ${authenticationCredentials.accessToken}") }
            val httpEntity = HttpEntity<String>("", request)

            When("email 과 loginPassword 를 이용해 로그아웃을 진행한 후") {
                val actual = restTemplate.postForEntity<String>("http://localhost:$port/api/auth/logout", httpEntity)

                Then("200 코드를 반환한다") {
                    actual.statusCode shouldBe HttpStatus.OK
                }

                Then("토큰을 삭제한다") {
                    val token = tokenQueryRepository.findByJti(jti)
                    token.shouldBeNull()
                }
            }
        }
    }
}
