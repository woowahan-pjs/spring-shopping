package shopping.wish.application

import io.kotest.matchers.nulls.shouldNotBeNull
import org.springframework.beans.factory.annotation.Autowired
import shopping.member.fixture.MemberFixture
import shopping.member.infra.MemberJpaRepository
import shopping.support.KotestIntegrationTestSupport

class WishListQueryServiceIntegrationTest : KotestIntegrationTestSupport() {
    @Autowired
    private lateinit var service: WishListQueryService

    @Autowired
    private lateinit var memberJpaRepository: MemberJpaRepository

    init {
        Given("회원 ID 를 받아") {
            val member = memberJpaRepository.save(MemberFixture.`고객 1`.`회원 엔티티 생성`())

            When("해당 회원의 위시 리스트를 찾아") {
                val actual = service.findWishList(member.id)

                Then("반환 한다") {
                    actual.shouldNotBeNull()
                }
            }
        }
    }
}
