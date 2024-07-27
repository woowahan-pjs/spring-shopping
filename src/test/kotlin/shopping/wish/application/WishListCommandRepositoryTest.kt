package shopping.wish.application

import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import shopping.support.KotestIntegrationTestSupport
import shopping.wish.fixture.WishListFixture

class WishListCommandRepositoryTest : KotestIntegrationTestSupport() {
    @Autowired
    private lateinit var repository: WishListCommandRepository

    init {
        Given("WishList 엔티티를 파라미터로 받아") {
            val wishList = WishListFixture.`고객 1의 위시 리스트`.`엔티티 생성`()

            When("DB 에 저장 한 후") {
                repository.save(wishList)

                Then("엔티티 객체의 ID 는 유효한 값이다") {
                    wishList.id shouldNotBe 0
                }
            }
        }
    }
}
