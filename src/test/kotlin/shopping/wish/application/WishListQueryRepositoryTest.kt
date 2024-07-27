package shopping.wish.application

import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import org.springframework.beans.factory.annotation.Autowired
import shopping.member.fixture.MemberFixture
import shopping.member.infra.MemberJpaRepository
import shopping.support.KotestIntegrationTestSupport
import shopping.wish.domain.WishList
import shopping.wish.infra.WishListJpaRepository

class WishListQueryRepositoryTest : KotestIntegrationTestSupport() {
    @Autowired
    private lateinit var repository: WishListQueryRepository

    @Autowired
    private lateinit var wishListJpaRepository: WishListJpaRepository

    @Autowired
    private lateinit var memberJpaRepository : MemberJpaRepository

    init {
        Given("전달 받은 회원 ID 를 이용해") {
            val member = MemberFixture.`고객 1`.`회원 엔티티 생성`()
            memberJpaRepository.save(member)

            When("회원 ID 가 일치하는 위시 리스트가 없다면") {
                val actual = repository.findByMemberIdAndNotDeleted(member.id)

                Then("null 을 반환 한다") {
                    actual.shouldBeNull()
                }
            }

            When("회원 ID 가 일치하고 삭제 되지 않은 위시 리스트가 있다면") {
                val wishList = wishListJpaRepository.save(WishList(member.id))

                val actual = repository.findByMemberIdAndNotDeleted(member.id)

                Then("엔티티를 반환 한다") {
                    actual.shouldNotBeNull()
                }
            }
        }
    }
}
