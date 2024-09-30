package shopping.auth.application

import io.kotest.matchers.equality.shouldBeEqualUsingFields
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import org.springframework.beans.factory.annotation.Autowired
import shopping.auth.fixture.TokenFixture
import shopping.auth.infra.TokenJpaRepository
import shopping.support.KotestIntegrationTestSupport

class TokenQueryRepositoryTest : KotestIntegrationTestSupport() {
    @Autowired
    private lateinit var repository: TokenQueryRepository

    @Autowired
    private lateinit var tokenJpaRepository: TokenJpaRepository

    init {
        Given("jti 를 이용해 토큰을 조회할 때") {
            val tokenFixture = TokenFixture.`토큰 1`
            val authenticationCredentials = tokenFixture.`토큰 엔티티 생성`()
            val jti = tokenFixture.jti

            When("저장된 토큰 중 동일한 jti 를 가진 토큰을 찾으면") {
                tokenJpaRepository.save(authenticationCredentials)
                val actual = repository.findByJti(jti!!)

                Then("토큰을 반환 한다") {
                    actual.shouldNotBeNull() shouldBeEqualUsingFields authenticationCredentials
                }
            }

            When("저장된 토큰 중 동일한 jti 를 가진 토큰이 없다면") {
                val actual = repository.findByJti(jti!!)

                Then("null 을 반환 한다") {
                    actual.shouldBeNull()
                }
            }
        }
    }
}
