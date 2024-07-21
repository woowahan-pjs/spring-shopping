package shopping.auth.application

import io.kotest.core.annotation.DisplayName
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import shopping.auth.fixture.TokenFixture
import shopping.auth.infra.TokenJpaRepository
import shopping.support.KotestIntegrationTestSupport

@DisplayName("TokenCommandRepository 테스트")
class TokenCommandRepositoryTest : KotestIntegrationTestSupport() {
    @Autowired
    private lateinit var repository: TokenCommandRepository

    @Autowired
    private lateinit var tokenJpaRepository: TokenJpaRepository

    init {
        Given("새로운 토큰 객체를 저장할 때") {
            val authenticationCredentials = TokenFixture.`토큰 1`.`토큰 엔티티 생성`()

            When("저장에 성공 하면") {
                repository.save(authenticationCredentials)

                Then("정상 적인 ID 를 생성 한다.") {
                    authenticationCredentials.id shouldNotBe 0L
                }
            }
        }
    }
}
